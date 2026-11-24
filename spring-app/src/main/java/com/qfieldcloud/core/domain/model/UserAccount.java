package com.qfieldcloud.core.domain.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Duration;
import java.time.Instant;

/**
 * Stores non-critical user information such as avatar, bio, timezone settings etc.
 * Maps Django's UserAccount model (OneToOne with User).
 */
@Entity
@Table(name = "core_useraccount")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user"})
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserAccount {

    public static final Duration NOTIFS_IMMEDIATELY = Duration.ZERO;
    public static final Duration NOTIFS_HOURLY = Duration.ofHours(1);
    public static final Duration NOTIFS_DAILY = Duration.ofDays(1);
    public static final Duration NOTIFS_WEEKLY = Duration.ofDays(7);
    public static final Duration NOTIFS_DISABLED = null;

    @Id
    @Column(name = "user_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "user_id", nullable = false)
    @EqualsAndHashCode.Include
    private User user;

    @Column(name = "db_limit_mb", nullable = false)
    @Builder.Default
    private Integer dbLimitMb = 25;

    @Column(length = 255, nullable = false)
    @Builder.Default
    private String bio = "";

    @Column(length = 255, nullable = false)
    @Builder.Default
    private String company = "";

    @Column(length = 255, nullable = false)
    @Builder.Default
    private String location = "";

    @Column(name = "is_email_public", nullable = false)
    @Builder.Default
    private Boolean isEmailPublic = false;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "notifs_frequency")
    private Duration notifsFrequency;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;
}