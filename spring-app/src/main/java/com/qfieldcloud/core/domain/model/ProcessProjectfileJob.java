package com.qfieldcloud.core.domain.model;

import jakarta.persistence.Entity;

/**
 * Job for processing a QGIS project file. Maps Django's ProcessProjectfileJob model.
 */
@Entity
@jakarta.persistence.DiscriminatorValue("process_projectfile")
public class ProcessProjectfileJob extends Job {
    public ProcessProjectfileJob() {
        setType(Type.PROCESS_PROJECTFILE.getValue());
    }
}