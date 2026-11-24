package com.qfieldcloud.core.application.dto;

import lombok.*;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrganizationMemberDto {
    private Long id;
    private Long organizationId;
    private String organizationUsername;
    private Long memberId;
    private String memberUsername;
    private String role;
    private boolean isPublic;
    private Instant createdAt;
    private Instant updatedAt;
}