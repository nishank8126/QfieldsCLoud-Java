package com.qfieldcloud.authentication.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank
    private String username; // can be username or email

    @NotBlank
    private String password;
}