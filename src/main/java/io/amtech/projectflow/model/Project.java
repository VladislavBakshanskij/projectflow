package io.amtech.projectflow.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class Project {
    private UUID id;
    private String name;
    private UUID projectLeadId;
    private UUID directionId;
    private Instant createDate;
    private ProjectStatus status;
}
