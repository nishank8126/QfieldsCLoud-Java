package com.qfieldcloud.core.application.security;

import com.qfieldcloud.core.application.exception.NotAuthenticatedException;
import com.qfieldcloud.core.domain.model.User;
import com.qfieldcloud.core.domain.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * Resolves the authenticated {@link User} entity from the security context.
 */
@Component
public class CurrentUserProvider {

    private final UserRepository userRepository;

    public CurrentUserProvider(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User require(Authentication authentication) {
        User user = optional(authentication);
        if (user == null) {
            throw new NotAuthenticatedException();
        }
        return user;
    }

    public User optional(Authentication authentication) {
        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof UserDetails principal)) {
            return null;
        }
        return userRepository.findByUsername(principal.getUsername()).orElse(null);
    }
}
