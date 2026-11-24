package com.qfieldcloud.core.domain.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@jakarta.persistence.DiscriminatorValue("delta_apply")
@Getter @Setter
@AllArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class ApplyJob extends Job {

    @Column(name = "overwrite_conflicts")
    @Builder.Default
    private Boolean overwriteConflicts = true;

    public ApplyJob() {
        setType(Type.DELTA_APPLY.getValue());
    }
}