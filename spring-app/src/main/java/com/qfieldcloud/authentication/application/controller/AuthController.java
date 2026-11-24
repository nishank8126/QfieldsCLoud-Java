package com.qfieldcloud.authentication.application.controller;

import com.qfieldcloud.authentication.application.dto.LoginRequest;
import com.qfieldcloud.authentication.application.dto.LoginResponse;
import com.qfieldcloud.core.application.dto.UserDto;
import com.qfieldcloud.core.application.exception.AuthenticationFailedException;
import com.qfieldcloud.core.application.exception.NotAuthenticatedException;
import com.qfieldcloud.core.application.security.CurrentUserProvider;
import com.qfieldcloud.core.application.security.JwtService;
import com.qfieldcloud.core.domain.model.User;
import com.qfieldcloud.core.domain.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final CurrentUserProvider currentUserProvider;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService,
                          UserRepository userRepository,
                          CurrentUserProvider currentUserProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.currentUserProvider = currentUserProvider;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        } catch (AuthenticationException ex) {
            throw new AuthenticationFailedException("Invalid username or password.");
        }

        UserDetails principal = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new AuthenticationFailedException("Invalid username or password."));

        return ResponseEntity.ok(LoginResponse.builder()
                .token(jwtService.generateToken(principal))
                .tokenType("Token")
                .expiresIn(jwtService.getExpirationSeconds())
                .user(UserDto.fromEntity(user))
                .build());
    }

    @GetMapping("/user")
    public ResponseEntity<UserDto> currentUser(Authentication authentication) {
        User user = currentUserProvider.require(authentication);
        return ResponseEntity.ok(UserDto.fromEntity(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        // Stateless JWT: the client discards the token.
        return ResponseEntity.ok(Map.of("detail", "Successfully logged out."));
    }
}
