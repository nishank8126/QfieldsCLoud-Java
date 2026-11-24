package com.qfieldcloud.core.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Map;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateDeltaRequest {
    @NotNull
    private UUID projectId;

    @NotNull
    private UUID deltafileId;

    @NotNull
    private UUID clientId;

    private Map<String, Object> content;
}
