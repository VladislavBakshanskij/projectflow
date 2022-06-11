package io.amtech.projectflow.dto.request.project;

import io.amtech.projectflow.model.project.journal.EventType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

@Data
@Accessors(chain = true)
public class ProjectJournalEvent {
    private Event event;
    private EventType eventType;

    @Data
    @Accessors(chain = true)
    public static class Event {
        private String recipient;
        private String login;
        private String subject;
        private String projectName;
        private Map<String, Object> changes = new HashMap<>();
    }
}
