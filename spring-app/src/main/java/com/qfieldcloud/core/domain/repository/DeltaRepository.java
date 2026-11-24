package com.qfieldcloud.core.domain.repository;

import com.qfieldcloud.core.domain.model.Delta;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DeltaRepository extends JpaRepository<Delta, UUID> {
    List<Delta> findByProjectId(UUID projectId);
    boolean existsById(UUID id);

    /** Paged so the Spring Batch reader can page through pending deltas in bulk. */
    Page<Delta> findByLastStatus(String lastStatus, Pageable pageable);
}