package com.qfieldcloud.core.domain.model;

import com.qfieldcloud.project.domain.model.Project;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Represents a delta (change set) applied to a project.
 * Maps Django's Delta model.
 */
@Entity
@Table(name = "core_delta")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"project", "createdBy", "lastApplyAttemptBy"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Delta {

    public enum Method {
        Create("create"), Delete("delete"), Patch("patch");

        private final String value;
        Method(String value) { this.value = value; }
    }

    @Getter
    public enum Status {
        PENDING("pending"), STARTED("started"), APPLIED("applied"),
        CONFLICT("conflict"), NOT_APPLIED("not_applied"), ERROR("error"),
        IGNORED("ignored"), UNPERMITTED("unpermitted");

        private final String value;
        Status(String value) { this.value = value; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(name = "deltafile_id", nullable = false)
    private UUID deltafileId;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "JSON")
    private Map<String, Object> content;

    @Column(name = "last_status", length = 32, nullable = false)
    @Builder.Default
    private String lastStatus = Status.PENDING.value;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "last_feedback", columnDefinition = "JSON")
    private Map<String, Object> lastFeedback;

    @Column(name = "last_modified_pk")
    private String lastModifiedPk;

    @Column(name = "last_apply_attempt_at")
    private Instant lastApplyAttemptAt;

    @Column(name = "last_apply_attempt_by_id")
    private Long lastApplyAttemptById;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "created_by_id")
    private Long createdById;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", insertable = false, updatable = false)
    private User createdBy;

    public String getShortId() {
        return id != null ? id.toString().substring(0, 8) : null;
    }
}