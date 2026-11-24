package com.qfieldcloud.core.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Link between Project and User (collaborator) with a role.
 * Maps Django's ProjectCollaborator model.
 */
@Entity
@Table(name = "core_projectcollaborator",
       uniqueConstraints = @UniqueConstraint(columnNames = {"project_id", "collaborator_id"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"project", "collaborator", "createdBy", "updatedBy"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ProjectCollaborator {

    public enum Roles {
        ADMIN("admin"), MANAGER("manager"), EDITOR("editor"), REPORTER("reporter"), READER("reader");

        private final String value;
        Roles(String value) { this.value = value; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private com.qfieldcloud.project.domain.model.Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "collaborator_id", nullable = false)
    private User collaborator;

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String role = Roles.READER.value;

    @Column(name = "is_incognito", nullable = false)
    @Builder.Default
    private Boolean isIncognito = false;

    @Column(name = "created_by_id")
    private Long createdById;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_by_id")
    private Long updatedById;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}