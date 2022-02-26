package io.amtech.projectflow.service.project.journal;

import io.amtech.projectflow.dto.response.project.journal.ProjectJournalDto;

import java.util.UUID;

public interface ProjectJournalService {
    ProjectJournalDto get(UUID projectId, UUID id);
}
