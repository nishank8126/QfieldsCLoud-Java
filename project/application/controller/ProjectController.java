package com.qfieldcloud.project.application.controller;

import com.qfieldcloud.project.application.dto.CreateProjectRequest;
import com.qfieldcloud.project.application.dto.ProjectDto;
import com.qfieldcloud.project.domain.model.Project;
import com.qfieldcloud.project.domain.repository.ProjectRepository;
import com.qfieldcloud.core.application.exception.ObjectNotFoundException;
import com.qfieldcloud.core.application.exception.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> listProjects() {
        List<ProjectDto> dtos = projectRepository.findAll().stream()
                .map(ProjectDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/public")
    public ResponseEntity<List<ProjectDto>> listPublicProjects() {
        List<ProjectDto> dtos = projectRepository.findByOwnerIdAndIsPublic(null, true).stream()
                .map(ProjectDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable UUID id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Project not found: " + id));
        return ResponseEntity.ok(ProjectDto.fromEntity(project));
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@RequestBody CreateProjectRequest request) {
        if (projectRepository.existsByOwnerIdAndNameIgnoreCase(
                1L, request.getName())) { // TODO: use actual owner from auth context
            throw new ValidationException("Project with the same name already exists.");
        }

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setIsPublic(request.getIsPublic() != null ? request.getIsPublic() : false);
        project.setPublicCollaboratorRole(request.getPublicCollaboratorRole() != null ?
                request.getPublicCollaboratorRole() : "reader");
        project.setProjectType(request.getProjectType() != null ? request.getProjectType() : "regular");
        project.setOverwriteConflicts(request.getOverwriteConflicts() != null ? request.getOverwriteConflicts() : true);
        project.setHasRestrictedProjectfiles(request.getHasRestrictedProjectfiles() != null ?
                request.getHasRestrictedProjectfiles() : false);
        // TODO: set owner from authenticated user

        Project saved = projectRepository.save(project);
        return ResponseEntity.ok(ProjectDto.fromEntity(saved));
    }
}