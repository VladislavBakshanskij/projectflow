package io.amtech.projectflow.publisher.project;

import io.amtech.projectflow.dto.request.project.ProjectJournalEvent;
import io.amtech.projectflow.publisher.EventPublisher;

public interface ProjectEventPublisher extends EventPublisher<ProjectJournalEvent> {
}
