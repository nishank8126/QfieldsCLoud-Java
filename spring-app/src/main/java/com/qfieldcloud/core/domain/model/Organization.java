package com.qfieldcloud.core.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Organization user type. Maps Django's Organization model.
 * createdAt/updatedAt are inherited from {@link User}.
 */
@Entity
@DiscriminatorValue("2")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Organization extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organization_owner_id")
    private User organizationOwner;

    @Column(name = "is_initially_trial")
    @Builder.Default
    private Boolean isInitiallyTrial = false;

    @Column(name = "default_project_role_for_members", length = 10)
    private String defaultProjectRoleForMembers;

    @Column(name = "created_by_id")
    private Long createdById;
}