package io.amtech.projectflow.model.project;

import io.amtech.projectflow.error.DataNotFoundException;
import lombok.NonNull;
import org.jooq.EnumType;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public enum ProjectStatus implements EnumType {
    UNAPPROVED,
    ON_PL_PLANNING,
    ON_DL_APPROVING,
    ON_DIRECTOR_APPROVING,
    DIRECTOR_APPROVED,
    DONE;

    private static final Map<String, ProjectStatus> STATUSES = Arrays.stream(values())
            .collect(Collectors.toMap(ProjectStatus::name, Function.identity()));

    public static ProjectStatus of(final String status) {
        final String validatedStatus = Objects.requireNonNull(status, "Статус не может быть пустой");
        final ProjectStatus projectStatus = STATUSES.get(validatedStatus.toUpperCase());
        if (Objects.isNull(projectStatus)) {
            throw new DataNotFoundException("Статус проекта не найден " + status);
        }
        return projectStatus;
    }

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
