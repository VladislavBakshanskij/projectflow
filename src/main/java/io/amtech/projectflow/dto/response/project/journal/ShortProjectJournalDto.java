package io.amtech.projectflow.dto.response.project.journal;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ShortProjectJournalDto {
    private UUID id;
    private UUID projectId;
    private String login;
    private Instant updateDate;
}
