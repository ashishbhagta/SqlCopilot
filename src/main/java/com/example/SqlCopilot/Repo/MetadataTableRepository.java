package com.example.SqlCopilot.Repo;

import com.example.SqlCopilot.Entity.MetadataTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetadataTableRepository extends JpaRepository<MetadataTable, Long> {
    MetadataTable findByTableName(String tableName);
}

