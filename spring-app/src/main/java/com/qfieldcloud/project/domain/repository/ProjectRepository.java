package com.qfieldcloud.project.domain.repository;

import com.qfieldcloud.project.domain.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {
    Optional<Project> findById(UUID id);
    List<Project> findByOwnerId(Long ownerId);
    List<Project> findByIsPublicTrue();
    List<Project> findByOwnerIdAndIsPublic(Long ownerId, Boolean isPublic);
    boolean existsByOwnerIdAndNameIgnoreCase(Long ownerId, String name);
}