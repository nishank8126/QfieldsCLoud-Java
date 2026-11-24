package com.qfieldcloud.authentication.application.dto;

import lombok.*;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenResponse {
    private String token;
    private Instant expiresAt;
    private String username;
    private String type;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String avatarUrl;
}