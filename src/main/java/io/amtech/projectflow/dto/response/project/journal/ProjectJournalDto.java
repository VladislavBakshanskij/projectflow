package io.amtech.projectflow.dto.response.project.journal;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ProjectJournalDto {
    private UUID id;
    private UUID projectId;
    private Instant updateDate;
    private String login;
    private Map<String, Object> currentState = new HashMap<>();
}
