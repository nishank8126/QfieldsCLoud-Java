package com.qfieldcloud.core.application.dto;

import lombok.*;

import java.time.Instant;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectCollaboratorDto {
    private Long id;
    private UUID projectId;
    private Long collaboratorId;
    private String collaboratorUsername;
    private String role;
    private boolean isIncognito;
    private Long createdById;
    private Instant createdAt;
    private Instant updatedAt;
}