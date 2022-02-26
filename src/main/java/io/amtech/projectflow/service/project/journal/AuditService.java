package io.amtech.projectflow.service.project.journal;

import io.amtech.projectflow.listener.event.JournalEventType;

public interface AuditService<T> {
    JournalEventType getSupportType();

    void save(T data);
}
