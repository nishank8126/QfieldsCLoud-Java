package com.qfieldcloud.core.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Base user model. Maps Django's qfieldcloud.core.models.User (AbstractUser).
 * Inheritance type: SINGLE_TABLE with discriminator column "user_type".
 * Subclasses: Person, Organization, Team.
 */
@Entity
@Table(name = "core_user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.INTEGER)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = {"password"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class User {

    public enum Type {
        PERSON(1),
        ORGANIZATION(2),
        TEAM(3);

        private final int value;
        Type(int value) { this.value = value; }
        public int getValue() { return value; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, unique = true, length = 150)
    @NotBlank
    @Size(min = 3, max = 150)
    @Pattern(regexp = "^[-a-zA-Z0-9_]+$", message = "Only letters, numbers, underscores '_' or hyphens '-' are allowed.")
    private String username;

    @Column(nullable = false)
    @NotBlank
    private String password;

    @Column(length = 150)
    private String firstName;

    @Column(length = 150)
    private String lastName;

    @Column(unique = true, length = 254)
    @Email
    private String email;

    @Column(nullable = false)
    private boolean isStaff = false;

    @Column(nullable = false)
    private boolean isActive = true;

    @Column(name = "created_at")
    @CreationTimestamp
    private Instant createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private Instant updatedAt;

    @Transient
    public Type getType() {
        if (this instanceof Organization) return Type.ORGANIZATION;
        if (this instanceof Team) return Type.TEAM;
        return Type.PERSON;
    }

    @Transient
    public boolean isPerson() { return getType() == Type.PERSON; }

    @Transient
    public boolean isOrganization() { return getType() == Type.ORGANIZATION; }

    @Transient
    public boolean isTeam() { return getType() == Type.TEAM; }

    @Transient
    public String fullName() {
        return (firstName != null ? firstName : "") + " " + (lastName != null ? lastName : "").trim();
    }

    @Transient
    public String usernameWithFullName() {
        String fullName = fullName().strip();
        return fullName.isEmpty() ? username : username + " (" + fullName + ")";
    }
}