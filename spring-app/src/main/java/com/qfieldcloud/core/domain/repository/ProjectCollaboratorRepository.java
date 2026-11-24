package com.qfieldcloud.core.domain.repository;

import com.qfieldcloud.core.domain.model.ProjectCollaborator;
import com.qfieldcloud.project.domain.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectCollaboratorRepository extends JpaRepository<ProjectCollaborator, Long> {
    List<ProjectCollaborator> findByProjectId(java.util.UUID projectId);
    List<ProjectCollaborator> findByCollaboratorId(Long collaboratorId);
    Optional<ProjectCollaborator> findByProjectIdAndCollaboratorId(java.util.UUID projectId, Long collaboratorId);
    boolean existsByProjectIdAndCollaboratorId(java.util.UUID projectId, Long collaboratorId);
}