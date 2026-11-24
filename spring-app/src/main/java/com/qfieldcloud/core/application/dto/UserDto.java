package com.qfieldcloud.core.application.dto;

import com.qfieldcloud.core.domain.model.User;
import lombok.*;

import java.time.Instant;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private String fullName;
    private String type; // PERSON, ORGANIZATION, TEAM
    private boolean isStaff;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;

    public static UserDto fromEntity(User user) {
        if (user == null) return null;
        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.fullName())
                .type(user.getType().name())
                .isStaff(user.isStaff())
                .isActive(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}