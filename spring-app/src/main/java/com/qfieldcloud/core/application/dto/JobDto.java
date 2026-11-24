package com.qfieldcloud.core.application.dto;

import com.qfieldcloud.core.domain.model.Job;
import lombok.*;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobDto {
    private UUID id;
    private UUID projectId;
    private String type;
    private String status;
    private String output;
    private Map<String, Object> feedback;
    private Long triggeredById;
    private Long createdById;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant startedAt;
    private Instant finishedAt;
    private String containerId;

    public static JobDto fromEntity(Job job) {
        if (job == null) return null;
        return JobDto.builder()
                .id(job.getId())
                .projectId(job.getProject() != null ? job.getProject().getId() : null)
                .type(job.getType())
                .status(job.getStatus())
                .output(job.getOutput())
                .feedback(job.getFeedback())
                .triggeredById(job.getTriggeredById())
                .createdById(job.getCreatedById())
                .createdAt(job.getCreatedAt())
                .updatedAt(job.getUpdatedAt())
                .startedAt(job.getStartedAt())
                .finishedAt(job.getFinishedAt())
                .containerId(job.getContainerId())
                .build();
    }
}