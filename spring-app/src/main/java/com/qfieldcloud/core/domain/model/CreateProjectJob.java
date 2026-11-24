package com.qfieldcloud.core.domain.model;

import jakarta.persistence.Entity;

/**
 * Job for creating a project. Maps Django's CreateProjectJob (base Job with CREATE_PROJECT type).
 */
@Entity
@jakarta.persistence.DiscriminatorValue("create_project")
public class CreateProjectJob extends Job {
    public CreateProjectJob() {
        setType(Type.CREATE_PROJECT.getValue());
    }
}