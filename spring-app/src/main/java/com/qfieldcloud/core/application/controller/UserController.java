package com.qfieldcloud.core.application.controller;

import com.qfieldcloud.core.application.dto.UserDto;
import com.qfieldcloud.core.application.exception.ObjectNotFoundException;
import com.qfieldcloud.core.domain.model.Organization;
import com.qfieldcloud.core.domain.model.User;
import com.qfieldcloud.core.domain.repository.OrganizationMemberRepository;
import com.qfieldcloud.core.domain.repository.OrganizationRepository;
import com.qfieldcloud.core.domain.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserRepository userRepository;
    private final OrganizationRepository organizationRepository;
    private final OrganizationMemberRepository memberRepository;

    public UserController(UserRepository userRepository,
                          OrganizationRepository organizationRepository,
                          OrganizationMemberRepository memberRepository) {
        this.userRepository = userRepository;
        this.organizationRepository = organizationRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> listUsers(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) String organization) {
        List<User> users;
        if (organization != null && !organization.isBlank()) {
            Organization org = organizationRepository.findByUsername(organization)
                    .orElseThrow(() -> new ObjectNotFoundException("Organization not found: " + organization));
            users = memberRepository.findByOrganizationId(org.getId()).stream()
                    .map(m -> m.getMember())
                    .collect(Collectors.toList());
        } else {
            users = userRepository.findAll();
        }

        if (q != null && !q.isBlank()) {
            String needle = q.toLowerCase(Locale.ROOT);
            users = users.stream()
                    .filter(u -> containsIgnoreCase(u.getUsername(), needle)
                            || containsIgnoreCase(u.getEmail(), needle)
                            || containsIgnoreCase(u.fullName(), needle))
                    .collect(Collectors.toList());
        }

        List<UserDto> dtos = users.stream()
                .map(UserDto::fromEntity)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private boolean containsIgnoreCase(String value, String needle) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(needle);
    }

    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ObjectNotFoundException("User not found: " + username));
        return ResponseEntity.ok(UserDto.fromEntity(user));
    }
}