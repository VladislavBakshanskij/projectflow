package io.amtech.projectflow.model.project.milestone;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Milestone {
    private UUID id;
    private UUID projectId;
    private String name;
    private String description;
    private Instant plannedStartDate;
    private Instant plannedFinishDate;
    private Instant factStartDate;
    private Instant factFinishDate;
    private Instant createDate;
    private short progressPercent = 0;
}
