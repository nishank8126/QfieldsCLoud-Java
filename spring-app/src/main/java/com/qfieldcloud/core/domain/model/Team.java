package com.qfieldcloud.core.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * Team user type. Maps Django's Team model.
 */
@Entity
@DiscriminatorValue("3")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Team extends User {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_organization_id")
    private Organization teamOrganization;
}