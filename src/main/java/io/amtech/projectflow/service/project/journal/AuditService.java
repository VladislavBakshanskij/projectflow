package io.amtech.projectflow.service.project.journal;

import io.amtech.projectflow.listener.event.JournalEventData;
import io.amtech.projectflow.listener.event.JournalEventType;

public interface AuditService<T extends JournalEventData> {
    JournalEventType getSupportType();

    void save(T data);
}
