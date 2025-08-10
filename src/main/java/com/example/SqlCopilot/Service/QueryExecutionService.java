package com.example.SqlCopilot.Service;

import com.example.SqlCopilot.Entity.BackupQuery;
import com.example.SqlCopilot.Util.SQLGenerator;
import com.example.SqlCopilot.dto.QueryRequest;
import com.example.SqlCopilot.dto.QueryResponse;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QueryExecutionService {

    private final AiService aiService;
    private final SQLGenerator sqlGenerator;
    private final JdbcTemplate jdbcTemplate;
    private final BackupService backupService;
    private final LoggingService loggingService;

    public QueryExecutionService(AiService aiService,
                                 SQLGenerator sqlGenerator,
                                 JdbcTemplate jdbcTemplate,
                                 BackupService backupService,
                                 LoggingService loggingService) {
        this.aiService = aiService;
        this.sqlGenerator = sqlGenerator;
        this.jdbcTemplate = jdbcTemplate;
        this.backupService = backupService;
        this.loggingService = loggingService;
    }

    /**
     * Only generate SQL text (no execution).
     */
    public String generateSql(QueryRequest request) {
        String promptWithContext = sqlGenerator.buildPrompt(request.getPrompt(), request.getDbType());
        return aiService.generateSql(promptWithContext, request.getDbType());
    }

    /**
     * Generate and optionally execute (if allowExecution true).
     * Performs safety checks and backup for destructive queries.
     */
    @Transactional
    public QueryResponse generateAndMaybeExecute(QueryRequest request) {
        String user = request.getUser() == null ? "anonymous" : request.getUser();
        String promptWithContext = sqlGenerator.buildPrompt(request.getPrompt(), request.getDbType());
        String generatedSql = aiService.generateSql(promptWithContext, request.getDbType());

        // basic safety checks
        if (containsDangerousStatements(generatedSql)) {
            String message = "Blocked unsafe statement. Execution not allowed.";
            loggingService.log(user, request.getPrompt(), generatedSql, request.getDbType().name(), message);
            return buildResponse(generatedSql, "blocked", message);
        }

        // If execution not allowed, just return generated SQL
        if (!request.isAllowExecution()) {
            loggingService.log(user, request.getPrompt(), generatedSql, request.getDbType().name(), "generated-only");
            return buildResponse(generatedSql, "generated", "Execution not requested");
        }

        // For UPDATE/DELETE we create a backup
        if (isUpdateOrDelete(generatedSql)) {
            // Build a SELECT for backup - naive approach: rewrite to SELECT * FROM ... WHERE ...
            String selectForBackup = sqlGenerator.buildSelectForBackup(generatedSql);
            BackupQuery backup = backupService.backupBefore(selectForBackup, generatedSql, user);
            // Execute the destructive query
            int rowsAffected = jdbcTemplate.update(generatedSql);
            String message = "Executed. Rows affected: " + rowsAffected + ". Backup id: " + backup.getId();
            loggingService.log(user, request.getPrompt(), generatedSql, request.getDbType().name(), message);
            return buildResponse(generatedSql, "executed", message);
        }

        // Non-destructive like SELECT -> execute and return results summary
        if (isSelect(generatedSql)) {
            List<?> rows = jdbcTemplate.queryForList(generatedSql);
            String summary = "Rows returned: " + rows.size();
            loggingService.log(user, request.getPrompt(), generatedSql, request.getDbType().name(), summary);
            // Not returning full rows here, just summary (you can expand)
            QueryResponse resp = buildResponse(generatedSql, "executed", summary);
            return resp;
        }

        // Fallback: do not execute unknown statements by default
        loggingService.log(user, request.getPrompt(), generatedSql, request.getDbType().name(), "no-execution");
        return buildResponse(generatedSql, "generated", "No execution performed");
    }

    private QueryResponse buildResponse(String sql, String status, String message) {
        QueryResponse r = new QueryResponse(sql, status);
        r.setMessage(message);
        return r;
    }

    private boolean containsDangerousStatements(String sql) {
        String s = sql == null ? "" : sql.toLowerCase();
        // block DDL and dangerous patterns
        if (s.contains("drop ") || s.contains("truncate ") || s.contains("alter ")) return true;
        // allow DELETE/UPDATE only if they have WHERE clause (basic check)
        if ((s.contains("delete ") || s.contains("update ")) && !s.contains(" where ")) return true;
        return false;
    }

    private boolean isSelect(String sql) {
        return sql != null && sql.trim().toLowerCase().startsWith("select");
    }

    private boolean isUpdateOrDelete(String sql) {
        if (sql == null) return false;
        String t = sql.trim().toLowerCase();
        return t.startsWith("update") || t.startsWith("delete");
    }

    public Object getHistory() {
        return loggingService.history();
    }
}

