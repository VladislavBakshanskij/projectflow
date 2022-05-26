package io.amtech.projectflow.repository.project.journal;

import io.amtech.projectflow.model.project.journal.ProjectJournal;

import java.util.List;
import java.util.UUID;

public interface ProjectJournalRepository {
    ProjectJournal findByIdWithProjectId(UUID projectId, UUID id);

    ProjectJournal save(ProjectJournal projectJournal);

    List<ProjectJournal> findByProjectId(UUID projectId);
}
