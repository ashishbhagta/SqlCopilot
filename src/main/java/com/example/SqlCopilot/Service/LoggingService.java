package com.example.SqlCopilot.Service;

import com.example.SqlCopilot.Entity.ExecutionLog;
import com.example.SqlCopilot.Repo.ExecutionLogRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class LoggingService {

    private final ExecutionLogRepository logRepository;

    public LoggingService(ExecutionLogRepository logRepository) {
        this.logRepository = logRepository;
    }

    public ExecutionLog log(String user, String prompt, String sql, String dbType, String resultSummary) {
        ExecutionLog el = new ExecutionLog();
        el.setUserName(user);
        el.setPrompt(prompt);
        el.setGeneratedSql(sql);
        el.setDbType(dbType);
        el.setResultSummary(resultSummary);
        el.setExecutedAt(Instant.now());
        return logRepository.save(el);
    }

    public List<ExecutionLog> history() {
        return logRepository.findAll();
    }
}

