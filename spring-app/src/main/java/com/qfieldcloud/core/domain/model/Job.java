package com.qfieldcloud.core.domain.model;

import com.qfieldcloud.project.domain.model.Project;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * Base job model for background processing.
 * Maps Django's Job model.
 */
@Entity
@Table(name = "core_job")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "job_type")
@DiscriminatorValue("job")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(exclude = {"project", "triggeredBy", "createdBy"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Job {

    @Getter
    public enum Type {
        PACKAGE("package"), DELTA_APPLY("delta_apply"),
        PROCESS_PROJECTFILE("process_projectfile"), CREATE_PROJECT("create_project");

        private final String value;
        Type(String value) { this.value = value; }
    }

    @Getter
    public enum Status {
        PENDING("pending"), QUEUED("queued"), STARTED("started"),
        FINISHED("finished"), STOPPED("stopped"), FAILED("failed");

        private final String value;
        Status(String value) { this.value = value; }

        public static final java.util.EnumSet<Status> UNSTARTED_STATUS = java.util.EnumSet.of(PENDING, QUEUED);
        public static final java.util.EnumSet<Status> UNFINISHED_STATUS = java.util.EnumSet.of(PENDING, QUEUED, STARTED);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @Column(name = "type", length = 32, nullable = false)
    private String type;

    @Column(name = "status", length = 32, nullable = false)
    @Builder.Default
    private String status = Status.PENDING.value;

    @Column(columnDefinition = "TEXT")
    private String output;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "feedback", columnDefinition = "JSON")
    private Map<String, Object> feedback;

    @Column(name = "triggered_by_id")
    private Long triggeredById;

    @Column(name = "created_by_id")
    private Long createdById;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @Column(name = "started_at")
    private Instant startedAt;

    @Column(name = "finished_at")
    private Instant finishedAt;

    @Column(name = "docker_started_at")
    private Instant dockerStartedAt;

    @Column(name = "docker_finished_at")
    private Instant dockerFinishedAt;

    @Column(name = "container_id", length = 64)
    private String containerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "triggered_by_id", insertable = false, updatable = false)
    private User triggeredBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by_id", insertable = false, updatable = false)
    private User createdBy;

    public String getShortId() {
        return id != null ? id.toString().substring(0, 8) : null;
    }

    public Type getJobType() {
        return Type.valueOf(type != null ? type.toUpperCase() : "PACKAGE");
    }
}