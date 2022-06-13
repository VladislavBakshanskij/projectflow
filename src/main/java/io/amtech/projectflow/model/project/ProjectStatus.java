package io.amtech.projectflow.model.project;

import io.amtech.projectflow.error.DataNotFoundException;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ProjectStatus {
    UNAPPROVED,
    ON_PL_PLANNING,
    ON_DL_APPROVING,
    ON_DIRECTOR_APPROVING,
    DIRECTOR_APPROVED,
    DONE;

    private static final Map<String, ProjectStatus> STATUSES = Arrays.stream(values())
            .collect(Collectors.toMap(ProjectStatus::name, Function.identity()));

    public static ProjectStatus from(final io.amtech.projectflow.jooq.enums.ProjectStatus status) {
        final io.amtech.projectflow.jooq.enums.ProjectStatus validatedStatus = Objects.requireNonNull(status, "Статус не может быть пустой");
        return from(validatedStatus.name());
    }

    public static ProjectStatus from(final String status) {
        final String validatedStatus = Objects.requireNonNull(status, "Статус не может быть пустой");
        final ProjectStatus projectStatus = STATUSES.get(validatedStatus.toUpperCase());
        if (Objects.isNull(projectStatus)) {
            throw new DataNotFoundException("Статус проекта не найден " + status);
        }
        return projectStatus;
    }

    public io.amtech.projectflow.jooq.enums.ProjectStatus toJooqStatus() {
        return io.amtech.projectflow.jooq.enums.ProjectStatus.valueOf(this.name());
    }
}
