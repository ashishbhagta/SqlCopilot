package com.example.SqlCopilot.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "metadata_table")
public class MetadataTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String schemaName;
    private String tableName;
    private String description;

    // store JSON or simple comma-separated columns for demo
    @Lob
    private String columnsJson;

    public MetadataTable() {}

    // getters & setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSchemaName() { return schemaName; }
    public void setSchemaName(String schemaName) { this.schemaName = schemaName; }

    public String getTableName() { return tableName; }
    public void setTableName(String tableName) { this.tableName = tableName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getColumnsJson() { return columnsJson; }
    public void setColumnsJson(String columnsJson) { this.columnsJson = columnsJson; }
}
