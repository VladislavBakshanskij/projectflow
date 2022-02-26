package io.amtech.projectflow.model.employee;

import lombok.NonNull;
import org.jooq.EnumType;
import org.jooq.Schema;

import static io.amtech.projectflow.jooq.Pf.PF;

public enum UserPosition implements EnumType {
    PROJECT_LEAD,
    DIRECTION_LEAD,
    DIRECTOR;

    @Override
    @NonNull
    public String getLiteral() {
        return this.name();
    }

    @Override
    public String getName() {
        return "user_position";
    }

    @Override
    public Schema getSchema() {
        return PF;
    }
}
