package com.example.SqlCopilot.Entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "backup_query")
public class BackupQuery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String originalSql;

    @Lob
    private String backupData; // could be JSON of rows

    private String createdBy;
    private Instant createdAt;

    public BackupQuery() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getOriginalSql() { return originalSql; }
    public void setOriginalSql(String originalSql) { this.originalSql = originalSql; }

    public String getBackupData() { return backupData; }
    public void setBackupData(String backupData) { this.backupData = backupData; }

    public String getCreatedBy() { return createdBy; }
    public void setCreatedBy(String createdBy) { this.createdBy = createdBy; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

