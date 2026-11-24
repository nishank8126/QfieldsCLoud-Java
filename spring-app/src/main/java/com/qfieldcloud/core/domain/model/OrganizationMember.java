package com.qfieldcloud.core.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Membership link between Organization and User (member).
 * Maps Django's OrganizationMember model.
 */
@Entity
@Table(name = "core_organizationmember",
       uniqueConstraints = @UniqueConstraint(columnNames = {"organization_id", "member_id"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"organization", "member", "createdBy", "updatedBy"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class OrganizationMember {

    public enum Roles {
        ADMIN("admin"), CREATOR("creator"), MEMBER("member");

        private final String value;
        Roles(String value) { this.value = value; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "organization_id", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String role = Roles.MEMBER.value;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Boolean isPublic = false;

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