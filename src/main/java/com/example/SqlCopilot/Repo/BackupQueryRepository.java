package com.example.SqlCopilot.Repo;

import com.example.SqlCopilot.Entity.BackupQuery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BackupQueryRepository extends JpaRepository<BackupQuery, Long> {
}
