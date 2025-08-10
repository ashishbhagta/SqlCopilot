package com.example.SqlCopilot.Service;

import com.example.SqlCopilot.Entity.BackupQuery;
import com.example.SqlCopilot.Repo.BackupQueryRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Service
public class BackupService {

    private final JdbcTemplate jdbcTemplate;
    private final BackupQueryRepository backupRepo;

    public BackupService(JdbcTemplate jdbcTemplate, BackupQueryRepository backupRepo) {
        this.jdbcTemplate = jdbcTemplate;
        this.backupRepo = backupRepo;
    }

    /**
     * Execute a SELECT that represents the rows that would be affected and store them as backup JSON.
     * For demo purposes, we convert rows to a simple string representation.
     */
    public BackupQuery backupBefore(String selectSql, String originalSql, String username) {
        List<Map<String, Object>> rows = jdbcTemplate.queryForList(selectSql);

        // Convert rows to a simple string - in real app store JSON
        StringBuilder sb = new StringBuilder();
        for (Map<String, Object> r : rows) {
            sb.append(r.toString()).append("\n");
        }

        BackupQuery b = new BackupQuery();
        b.setOriginalSql(originalSql);
        b.setBackupData(sb.toString());
        b.setCreatedBy(username);
        b.setCreatedAt(Instant.now());
        return backupRepo.save(b);
    }
}

