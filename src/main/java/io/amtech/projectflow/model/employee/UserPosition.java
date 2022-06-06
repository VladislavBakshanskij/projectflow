package io.amtech.projectflow.model.employee;

import io.amtech.projectflow.error.DataNotFoundException;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum UserPosition {
    PROJECT_LEAD,
    DIRECTION_LEAD,
    DIRECTOR;

    private static final Map<String, UserPosition> USER_POSITION = Arrays.stream(values())
            .collect(Collectors.toMap(UserPosition::name, Function.identity()));

    public static UserPosition from(final io.amtech.projectflow.jooq.enums.UserPosition position) {
        return Optional.ofNullable(position)
                .map(io.amtech.projectflow.jooq.enums.UserPosition::name)
                .map(UserPosition::fromString)
                .orElseThrow(() -> new DataNotFoundException("Должность не найдена"));
    }

    public static UserPosition from(final String position) {
        return Optional.ofNullable(position)
                .map(UserPosition::fromString)
                .orElseThrow(() -> new DataNotFoundException("Должность не найдена"));
    }

    private static UserPosition fromString(final String position) {
        return USER_POSITION.get(position.toUpperCase());
    }

    public io.amtech.projectflow.jooq.enums.UserPosition toJooqStatus() {
        return io.amtech.projectflow.jooq.enums.UserPosition.valueOf(this.name());
    }
}
