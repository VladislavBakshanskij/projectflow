package io.amtech.projectflow.repository.project;

import io.amtech.projectflow.model.Project;

import java.util.UUID;

public interface ProjectRepository {
    void delete(UUID id);

    void checkOnExists(UUID id);

    Project findById(UUID id);

    void update(UUID id, Project project);

    Project save(Project project);
}
