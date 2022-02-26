package io.amtech.projectflow.model.project;

import lombok.NonNull;
import org.jooq.EnumType;

public enum ProjectStatus implements EnumType {
    UNAPPROVED,
    ON_PL_PLANNING,
    ON_DL_APPROVING,
    ON_DIRECTOR_APPROVING,
    DIRECTOR_APPROVED,
    DONE;

    @Override
    @NonNull
    public String getLiteral() {
        return this.name();
    }

    @Override
    public String getName() {
        return "project_status";
    }
}
