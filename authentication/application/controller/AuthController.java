package com.qfieldcloud.authentication.application.controller;

import com.qfieldcloud.authentication.application.dto.LoginRequest;
import com.qfieldcloud.authentication.application.dto.TokenResponse;
import com.qfieldcloud.core.application.exception.AuthenticationFailedException;
import com.qfieldcloud.core.domain.model.User;
import com.qfieldcloud.core.domain.repository.UserRepository;
import com.qfieldcloud.core.application.security.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(request.getUsername())
                .password("")
                .roles("USER")
                .build();

        String jwt = jwtService.generateToken(userDetails);
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AuthenticationFailedException("User not found"));

        TokenResponse response = TokenResponse.builder()
                .token(jwt)
                .username(user.getUsername())
                .type(user.getType().name())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.fullName())
                .build();

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        // In a stateless JWT setup, logout is client-side (discard token)
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<TokenResponse> getCurrentUser(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Token ")) {
            throw new AuthenticationFailedException("Missing token");
        }
        String token = authHeader.substring(6);
        String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new AuthenticationFailedException("User not found"));

        TokenResponse response = TokenResponse.builder()
                .token(token)
                .username(user.getUsername())
                .type(user.getType().name())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .fullName(user.fullName())
                .build();

        return ResponseEntity.ok(response);
    }
}