package com.qfieldcloud.core.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateJobRequest {
    @NotNull
    private UUID projectId;

    @NotBlank
    private String type; // package, delta_apply, process_projectfile, create_project

    private Integer force; // 0 or 1

    private Boolean overwriteConflicts;
}