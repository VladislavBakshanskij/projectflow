package io.amtech.projectflow.dto.response;

import io.amtech.projectflow.model.project.ProjectStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ProjectSavedDto {
    private UUID id;
    private String name;
    private String description;
    private Instant createDate;
    private ProjectStatus projectStatus;
    private UUID projectLeadId;
    private UUID directionId;
}
