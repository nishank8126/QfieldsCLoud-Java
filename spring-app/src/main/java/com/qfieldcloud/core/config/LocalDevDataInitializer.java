package com.qfieldcloud.core.config;

import com.qfieldcloud.core.domain.model.Person;
import com.qfieldcloud.core.domain.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Development-only seed data, active with the "local" profile.
 * Creates a demo person if the database is empty so login can be exercised.
 */
@Slf4j
@Component
@Profile("local")
public class LocalDevDataInitializer implements CommandLineRunner {

    public static final String DEMO_USERNAME = "demo";
    public static final String DEMO_PASSWORD = "demo12345";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LocalDevDataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (userRepository.existsByUsername(DEMO_USERNAME)) {
            return;
        }
        Person demo = Person.builder()
                .username(DEMO_USERNAME)
                .password(passwordEncoder.encode(DEMO_PASSWORD))
                .email("demo@example.com")
                .firstName("Demo")
                .lastName("Surveyor")
                .isActive(true)
                .isStaff(false)
                .build();
        userRepository.save(demo);
        log.info("Seeded local demo user '{}' with password '{}'", DEMO_USERNAME, DEMO_PASSWORD);
    }
}
