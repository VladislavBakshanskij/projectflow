package io.amtech.projectflow.dto.response.project;

import io.amtech.projectflow.model.ProjectStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ProjectDto {
    private UUID id;
    private String name;
    private UUID projectLeadId;
    private UUID directionId;
    private String description;
    private Instant createDate;
    private ProjectStatus projectStatus;
}
