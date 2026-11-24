package com.qfieldcloud.core.application.dto;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    @NotBlank
    @Size(min = 3, max = 150)
    @Pattern(regexp = "^[-a-zA-Z0-9_]+$")
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Email
    private String email;

    @Size(max = 150)
    private String firstName;

    @Size(max = 150)
    private String lastName;
}