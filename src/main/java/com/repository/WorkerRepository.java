package com.repository;

import com.orm.WorkerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WorkerRepository extends JpaRepository<WorkerInfo, Long> {
    Optional<WorkerInfo> findByWorkerName(String workerName);
}