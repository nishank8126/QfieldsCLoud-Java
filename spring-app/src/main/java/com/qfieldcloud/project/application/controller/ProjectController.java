package com.qfieldcloud.project.application.controller;

import com.qfieldcloud.core.application.exception.ObjectNotFoundException;
import com.qfieldcloud.core.application.exception.PermissionDeniedException;
import com.qfieldcloud.core.application.exception.ValidationException;
import com.qfieldcloud.core.application.security.CurrentUserProvider;
import com.qfieldcloud.core.domain.model.User;
import com.qfieldcloud.core.domain.repository.ProjectCollaboratorRepository;
import com.qfieldcloud.project.application.dto.CreateProjectRequest;
import com.qfieldcloud.project.application.dto.ProjectDto;
import com.qfieldcloud.project.application.service.ProjectCacheService;
import com.qfieldcloud.project.domain.model.Project;
import com.qfieldcloud.project.domain.repository.ProjectRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/v1/projects")
public class ProjectController {

    private final ProjectRepository projectRepository;
    private final ProjectCollaboratorRepository collaboratorRepository;
    private final CurrentUserProvider currentUserProvider;
    private final ProjectCacheService projectCacheService;

    public ProjectController(ProjectRepository projectRepository,
                             ProjectCollaboratorRepository collaboratorRepository,
                             CurrentUserProvider currentUserProvider,
                             ProjectCacheService projectCacheService) {
        this.projectRepository = projectRepository;
        this.collaboratorRepository = collaboratorRepository;
        this.currentUserProvider = currentUserProvider;
        this.projectCacheService = projectCacheService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectDto>> listProjects(Authentication authentication) {
        User user = currentUserProvider.require(authentication);
        Map<UUID, Project> visible = new LinkedHashMap<>();
        projectRepository.findByOwnerId(user.getId()).forEach(p -> visible.put(p.getId(), p));
        collaboratorRepository.findByCollaboratorId(user.getId())
                .forEach(c -> visible.put(c.getProject().getId(), c.getProject()));
        projectRepository.findByIsPublicTrue().forEach(p -> visible.put(p.getId(), p));
        return ResponseEntity.ok(visible.values().stream().map(ProjectDto::fromEntity).toList());
    }

    @GetMapping("/public")
    public ResponseEntity<List<ProjectDto>> listPublicProjects() {
        return ResponseEntity.ok(projectRepository.findByIsPublicTrue().stream()
                .map(ProjectDto::fromEntity)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProjectDto> getProject(@PathVariable UUID id, Authentication authentication) {
        ProjectDto project = projectCacheService.findDtoById(id);
        if (project == null) {
            throw new ObjectNotFoundException("Project not found: " + id);
        }

        User user = currentUserProvider.optional(authentication);
        boolean canRead = project.isPublic()
                || (user != null && (user.getId().equals(project.getOwnerId())
                        || collaboratorRepository.existsByProjectIdAndCollaboratorId(id, user.getId())));
        if (!canRead) {
            throw new PermissionDeniedException("You do not have access to this project.");
        }
        return ResponseEntity.ok(project);
    }

    @PostMapping
    public ResponseEntity<ProjectDto> createProject(@Valid @RequestBody CreateProjectRequest request,
                                                    Authentication authentication) {
        User user = currentUserProvider.require(authentication);

        if (projectRepository.existsByOwnerIdAndNameIgnoreCase(user.getId(), request.getName())) {
            throw new ValidationException("A project with this name already exists for this owner.");
        }

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .owner(user)
                .isPublic(Boolean.TRUE.equals(request.getIsPublic()))
                .projectType(resolveProjectType(request.getProjectType()))
                .createdById(user.getId())
                .fileStorage("default")
                .attachmentsFileStorage("default")
                .build();

        Project saved = projectRepository.save(project);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectDto.fromEntity(saved));
    }

    private Long ownerId(Project project) {
        return project.getOwner() != null ? project.getOwner().getId() : null;
    }

    private String resolveProjectType(String requested) {
        if (requested == null || requested.isBlank()) {
            return Project.ProjectType.REGULAR.getValue();
        }
        for (Project.ProjectType type : Project.ProjectType.values()) {
            if (type.getValue().equals(requested)) {
                return type.getValue();
            }
        }
        throw new ValidationException("Unknown project type: " + requested);
    }
}
