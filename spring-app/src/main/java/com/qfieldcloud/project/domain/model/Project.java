package com.qfieldcloud.project.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

/**
 * Represents a QFieldCloud project.
 * Maps Django's project.models.Project model.
 */
@Entity
@Table(name = "project_project",
       uniqueConstraints = @UniqueConstraint(columnNames = {"owner_id", "name"}))
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"owner", "createdBy", "lastPackageJob"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Project {

    public enum Status {
        OK("ok"), BUSY("busy"), FAILED("failed");

        private final String value;
        Status(String value) { this.value = value; }
    }

    public enum StatusCode {
        OK("ok"),
        FAILED_PROCESS_PROJECTFILE("failed_process_projectfile"),
        TOO_MANY_COLLABORATORS("too_many_collaborators");

        private final String value;
        StatusCode(String value) { this.value = value; }
    }

    public enum PackagingOffliner {
        QGIScore("qgiscore"), PYTHONMINI("pythonmini");

        private final String value;
        PackagingOffliner(String value) { this.value = value; }
    }

    @Getter
    public enum ProjectType {
        REGULAR("regular"), SHARED_DATASETS("shared_datasets"), TEMPLATE("template");

        private final String value;
        ProjectType(String value) { this.value = value; }
    }

    public enum UniqueConstraintDeltaBehavior {
        CREATE_CONFLICT("create_conflict"), QGIS_DEFAULT("qgis_default");

        private final String value;
        UniqueConstraintDeltaBehavior(String value) { this.value = value; }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include
    private UUID id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 1000)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private com.qfieldcloud.core.domain.model.User owner;

    @Column(name = "is_public", nullable = false)
    @Builder.Default
    private Boolean isPublic = false;

    @Column(name = "public_collaborator_role", length = 10)
    @Builder.Default
    private String publicCollaboratorRole = "reader";

    @Column(name = "project_type", length = 32, nullable = false)
    @Builder.Default
    private String projectType = ProjectType.REGULAR.value;

    @Column(name = "unique_constraint_delta_behavior", length = 32)
    @Builder.Default
    private String uniqueConstraintDeltaBehavior = UniqueConstraintDeltaBehavior.CREATE_CONFLICT.value;

    @Column(name = "created_by_id")
    private Long createdById;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "file_storage_bytes", nullable = false)
    @Builder.Default
    private Long fileStorageBytes = 0L;

    @Column(name = "data_last_updated_at")
    private Instant dataLastUpdatedAt;

    @Column(name = "data_last_packaged_at")
    private Instant dataLastPackagedAt;

    @Column(name = "last_package_job_id")
    private UUID lastPackageJobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_package_job_id", insertable = false, updatable = false)
    private com.qfieldcloud.core.domain.model.PackageJob lastPackageJob;

    @Column(name = "repackaging_cache_expire")
    private Duration repackagingCacheExpire = Duration.ofMinutes(60);

    @Column(name = "overwrite_conflicts", nullable = false)
    @Builder.Default
    private Boolean overwriteConflicts = true;

    @Column(name = "has_restricted_projectfiles", nullable = false)
    @Builder.Default
    private Boolean hasRestrictedProjectfiles = false;

    @Column(name = "thumbnail", length = 1024)
    private String thumbnail;

    @Column(name = "storage_keep_versions")
    private Integer storageKeepVersions;

    @Column(name = "packaging_offliner", length = 100)
    @Builder.Default
    private String packagingOffliner = PackagingOffliner.PYTHONMINI.value;

    @Column(name = "file_storage", length = 100, nullable = false)
    private String fileStorage;

    @Column(name = "qgis_version", length = 100)
    private String qgisVersion;

    @Column(name = "file_storage_migrated_at")
    private Instant fileStorageMigratedAt;

    @Column(name = "locked_at")
    private Instant lockedAt;

    @Column(name = "is_featured", nullable = false)
    @Builder.Default
    private Boolean isFeatured = false;

    @Column(name = "attachments_file_storage", length = 100, nullable = false)
    private String attachmentsFileStorage;

    @Column(name = "is_attachment_download_on_demand", nullable = false)
    @Builder.Default
    private Boolean isAttachmentDownloadOnDemand = false;

    @Column(name = "are_attachments_versioned", nullable = false)
    @Builder.Default
    private Boolean areAttachmentsVersioned = false;

    @Column(name = "restricted_data_last_updated_at")
    private Instant restrictedDataLastUpdatedAt;

    public boolean isRegularProject() {
        return ProjectType.REGULAR.value.equals(projectType);
    }

    public boolean isSharedDatasetsProject() {
        return ProjectType.SHARED_DATASETS.value.equals(projectType);
    }

    public boolean isTemplateProject() {
        return ProjectType.TEMPLATE.value.equals(projectType);
    }

    public boolean isPrivate() {
        return !isPublic;
    }

    public boolean hasTheQgisFile() {
        return false; // To be implemented with filestorage integration
    }
}