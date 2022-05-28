package io.amtech.projectflow.repository.project.comment;

import io.amtech.projectflow.model.project.ProjectComment;

import java.util.List;
import java.util.UUID;

public interface ProjectCommentRepository {
    List<ProjectComment> findByProjectId(UUID projectId);

    ProjectComment save(ProjectComment commentToSave);
}
