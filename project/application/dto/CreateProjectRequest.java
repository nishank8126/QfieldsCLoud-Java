package com.qfieldcloud.project.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateProjectRequest {
    @NotBlank
    @Size(max = 255)
    @Pattern(regexp = "^[a-zA-Z0-9-_.]+$")
    private String name;

    @Size(max = 1000)
    private String description;

    private Boolean isPublic;

    private String publicCollaboratorRole;

    private String projectType;

    private Boolean overwriteConflicts;

    private Boolean hasRestrictedProjectfiles;
}