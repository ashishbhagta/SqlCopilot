package com.example.SqlCopilot.dto;

import com.example.SqlCopilot.Entity.DatabaseType;

public class QueryRequest {
    private String prompt;          // natural language prompt
    private DatabaseType dbType;    // ORACLE, MSSQL, DB2, H2, etc.
    private boolean allowExecution; // require explicit permission to execute
    private String user;            // username for audit

    public QueryRequest() {}

    public String getPrompt() { return prompt; }
    public void setPrompt(String prompt) { this.prompt = prompt; }

    public DatabaseType getDbType() { return dbType; }
    public void setDbType(DatabaseType dbType) { this.dbType = dbType; }

    public boolean isAllowExecution() { return allowExecution; }
    public void setAllowExecution(boolean allowExecution) { this.allowExecution = allowExecution; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }
}

