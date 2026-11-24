package com.qfieldcloud.project.application.dto;

import com.qfieldcloud.project.domain.model.Project;
import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectDto {
    private UUID id;
    private String name;
    private String description;
    private Long ownerId;
    private String ownerUsername;
    private boolean isPublic;
    private String publicCollaboratorRole;
    private String projectType;
    private boolean overwriteConflicts;
    private boolean hasRestrictedProjectfiles;
    private Instant createdAt;
    private Instant updatedAt;

    public static ProjectDto fromEntity(Project project) {
        if (project == null) return null;
        return ProjectDto.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .ownerId(project.getOwner() != null ? project.getOwner().getId() : null)
                .ownerUsername(project.getOwner() != null ? project.getOwner().getUsername() : null)
                .isPublic(project.getIsPublic())
                .publicCollaboratorRole(project.getPublicCollaboratorRole())
                .projectType(project.getProjectType())
                .overwriteConflicts(project.getOverwriteConflicts())
                .hasRestrictedProjectfiles(project.getHasRestrictedProjectfiles())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}