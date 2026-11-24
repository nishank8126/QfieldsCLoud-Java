package com.qfieldcloud.core.application.controller;

import com.qfieldcloud.core.application.dto.ProjectCollaboratorDto;
import com.qfieldcloud.core.domain.model.ProjectCollaborator;
import com.qfieldcloud.core.domain.repository.ProjectCollaboratorRepository;
import com.qfieldcloud.core.application.exception.ObjectNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/collaborators")
public class ProjectCollaboratorController {

    private final ProjectCollaboratorRepository collaboratorRepository;

    public ProjectCollaboratorController(ProjectCollaboratorRepository collaboratorRepository) {
        this.collaboratorRepository = collaboratorRepository;
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<List<ProjectCollaboratorDto>> listCollaborators(@PathVariable UUID projectId) {
        List<ProjectCollaborator> collaborators = collaboratorRepository.findByProjectId(projectId);
        List<ProjectCollaboratorDto> dtos = collaborators.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private ProjectCollaboratorDto toDto(ProjectCollaborator collab) {
        return ProjectCollaboratorDto.builder()
                .id(collab.getId())
                .projectId(collab.getProject() != null ? collab.getProject().getId() : null)
                .collaboratorId(collab.getCollaborator() != null ? collab.getCollaborator().getId() : null)
                .collaboratorUsername(collab.getCollaborator() != null ? collab.getCollaborator().getUsername() : null)
                .role(collab.getRole())
                .isIncognito(collab.getIsIncognito())
                .createdById(collab.getCreatedById())
                .createdAt(collab.getCreatedAt())
                .updatedAt(collab.getUpdatedAt())
                .build();
    }
}