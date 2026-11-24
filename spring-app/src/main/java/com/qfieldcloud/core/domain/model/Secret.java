package com.qfieldcloud.core.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.util.UUID;

/**
 * Encrypted secret (environment variable or pg_service) for projects/organizations.
 * Maps Django's Secret model.
 */
@Entity
@Table(name = "core_secret",
       uniqueConstraints = {
           @UniqueConstraint(name = "secret_project_type_name_assigned_to_uniq",
               columnNames = {"project_id", "type", "name", "assigned_to_id"}),
           @UniqueConstraint(name = "secret_organization_type_name_assigned_to_uniq",
               columnNames = {"organization_id", "type", "name", "assigned_to_id"})
       })
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"value", "project", "assignedTo", "organization", "createdBy"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Secret {

    public enum Type {
        PGSERVICE("pgservice"), ENVVAR("envvar");

        private final String value;
        Type(String value) { this.value = value; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(name = "type", length = 32, nullable = false)
    private String type;

    @Column(name = "project_id")
    private UUID projectId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", insertable = false, updatable = false)
    private com.qfieldcloud.project.domain.model.Project project;

    @Column(name = "assigned_to_id")
    private Long assignedToId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_id", insertable = false, updatable = false)
    private User assignedTo;

    @Column(name = "organization_id")
    private Long organizationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_id", insertable = false, updatable = false)
    private Organization organization;

    @Column(name = "created_by_id")
    private Long createdById;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "`value`", nullable = false, length = 4000)
    private String value; // encrypted value

    public Type getSecretType() {
        return Type.valueOf(type != null ? type.toUpperCase() : "ENVVAR");
    }
}