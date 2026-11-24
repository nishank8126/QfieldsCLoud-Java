package com.qfieldcloud.core.application.dto;

import com.qfieldcloud.core.domain.model.Delta;
import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DeltaDto {
    private UUID id;
    private UUID deltafileId;
    private UUID clientId;
    private UUID projectId;
    private Map<String, Object> content;
    private String lastStatus;
    private Map<String, Object> lastFeedback;
    private String lastModifiedPk;
    private Instant lastApplyAttemptAt;
    private Instant createdAt;
    private Instant updatedAt;
    private Long createdById;
    private String createdByUsername;

    public static DeltaDto fromEntity(Delta delta) {
        if (delta == null) return null;
        return DeltaDto.builder()
                .id(delta.getId())
                .deltafileId(delta.getDeltafileId())
                .clientId(delta.getClientId())
                .projectId(delta.getProject() != null ? delta.getProject().getId() : null)
                .content(delta.getContent())
                .lastStatus(delta.getLastStatus())
                .lastFeedback(delta.getLastFeedback())
                .lastModifiedPk(delta.getLastModifiedPk())
                .lastApplyAttemptAt(delta.getLastApplyAttemptAt())
                .createdAt(delta.getCreatedAt())
                .updatedAt(delta.getUpdatedAt())
                .createdById(delta.getCreatedById())
                .createdByUsername(delta.getCreatedBy() != null ? delta.getCreatedBy().getUsername() : null)
                .build();
    }
}