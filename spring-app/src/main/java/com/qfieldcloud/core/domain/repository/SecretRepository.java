package com.qfieldcloud.core.domain.repository;

import com.qfieldcloud.core.domain.model.Secret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SecretRepository extends JpaRepository<Secret, Long> {
    List<Secret> findByProjectId(UUID projectId);
    List<Secret> findByOrganizationId(Long organizationId);
    List<Secret> findByAssignedToId(Long assignedToId);
    Optional<Secret> findByProjectIdAndTypeAndNameAndAssignedToId(UUID projectId, String type, String name, Long assignedToId);
}