package com.qfieldcloud.core.domain.repository;

import com.qfieldcloud.core.domain.model.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/** Named BackgroundJobRepository (not JobRepository) to avoid colliding with Spring Batch's own "jobRepository" bean. */
@Repository
public interface BackgroundJobRepository extends JpaRepository<Job, UUID> {
    List<Job> findByProjectId(UUID projectId);
    List<Job> findByProjectIdAndType(UUID projectId, String type);
    boolean existsById(UUID id);
}