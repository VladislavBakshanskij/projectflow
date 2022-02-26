package io.amtech.projectflow.dto.response.project.milestone;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class MilestoneDto {
    private UUID id;
    private String name;
    private String description;
    private Instant plannedStartDate;
    private Instant plannedFinishDate;
    private Instant factStartDate;
    private Instant factFinishDate;
    private short progressPercent;
}
