package io.amtech.projectflow.model.project;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ProjectJournal {
    private UUID id;
    private UUID projectId;
    private String login;
    private Instant updateDate;
    private Map<String, Object> currentState = new LinkedHashMap<>();
}
