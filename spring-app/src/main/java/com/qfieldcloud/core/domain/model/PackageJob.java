package com.qfieldcloud.core.domain.model;

import jakarta.persistence.Entity;

/**
 * Job for packaging a project. Maps Django's PackageJob model.
 */
@Entity
@jakarta.persistence.DiscriminatorValue("package")
public class PackageJob extends Job {
    public PackageJob() {
        setType(Type.PACKAGE.getValue());
    }
}