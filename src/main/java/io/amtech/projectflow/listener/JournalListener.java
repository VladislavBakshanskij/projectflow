package io.amtech.projectflow.listener;

import io.amtech.projectflow.listener.event.JournalEvent;
import io.amtech.projectflow.listener.event.JournalEventData;
import io.amtech.projectflow.listener.event.JournalEventType;
import io.amtech.projectflow.service.project.journal.AuditService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JournalListener {
    private final Map<JournalEventType, AuditService<?>> auditServices;

    public JournalListener(final List<AuditService<?>> auditServices) {
        this.auditServices = auditServices.stream()
                .collect(Collectors.toMap(AuditService::getSupportType, Function.identity()));
    }

    @EventListener(JournalEvent.class)
    @Async
    public <T extends JournalEventData> void onJournal(final JournalEvent<T> event) {
        AuditService auditService = auditServices.get(event.getType());
        if (Objects.isNull(auditService)) {
            log.warn("Неподдерживаемое событие :: {}", event.getType());
            return;
        }
        auditService.save(event.getData());
    }
}
