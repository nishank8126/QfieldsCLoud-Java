package com.qfieldcloud.core.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

/**
 * Membership link between Team and User (member).
 * Maps Django's TeamMember model.
 */
@Entity
@Table(name = "core_teammember",
       uniqueConstraints = @UniqueConstraint(columnNames = {"team_id", "member_id"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"team", "member"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TeamMember {

    public enum Roles {
        ADMIN("admin"), MEMBER("member");

        private final String value;
        Roles(String value) { this.value = value; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "team_id", nullable = false)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private User member;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}