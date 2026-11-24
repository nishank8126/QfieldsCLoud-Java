package com.qfieldcloud.authentication.application.dto;

import com.qfieldcloud.core.application.dto.UserDto;
import lombok.*;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String token;
    private String tokenType;
    private long expiresIn;
    private UserDto user;
}
