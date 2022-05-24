package io.amtech.projectflow.model.employee;

import io.amtech.projectflow.error.DataNotFoundException;
import lombok.NonNull;
import org.jooq.EnumType;
import org.jooq.Schema;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.amtech.projectflow.jooq.Pf.PF;

public enum UserPosition implements EnumType {
    PROJECT_LEAD,
    DIRECTION_LEAD,
    DIRECTOR,
    EMPLOYEE;

    private static final Map<String, UserPosition> positions = Arrays.stream(values())
            .collect(Collectors.toMap(UserPosition::name, Function.identity()));

    public static UserPosition from(final String position) {
        return Optional.ofNullable(positions.get(position.toUpperCase()))
                .orElseThrow(() -> new DataNotFoundException("Должность не найдена"));
    }

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
