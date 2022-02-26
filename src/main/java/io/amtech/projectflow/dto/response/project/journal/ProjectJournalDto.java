package io.amtech.projectflow.dto.response.project.journal;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

@Data
@Accessors(chain = true)
public class ProjectJournalDto {
    private Instant updateDate;
    private String login;
}
