package io.amtech.projectflow.repository.project.milesone;

import io.amtech.projectflow.model.project.milestone.Milestone;

import java.util.List;
import java.util.UUID;

public interface MilestoneRepository {
    Milestone findByIdWithProject(UUID projectId, UUID id);

    Milestone save(Milestone milestone);

    void checkOnExistsWithProject(UUID projectId, UUID id);

    void update(UUID id, Milestone milestone);

    void delete(UUID id);

    void updateProgress(UUID id, short progress);

    List<Milestone> findByProjectId(UUID projectId);
}
