package com.qfieldcloud.core.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Individual users in QFieldCloud. Maps Django's Person model.
 */
@Entity
@DiscriminatorValue("1")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Person extends User {

    @Column(name = "remaining_invitations")
    @Builder.Default
    private Integer remainingInvitations = 0;

    @Column(name = "remaining_trial_organizations")
    @Builder.Default
    private Integer remainingTrialOrganizations = 0;

    @Column(name = "has_newsletter_subscription")
    @Builder.Default
    private Boolean hasNewsletterSubscription = false;

    @Column(name = "has_accepted_tos")
    @Builder.Default
    private Boolean hasAcceptedTos = false;
}