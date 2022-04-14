package io.amtech.projectflow.dto.response.project;

import io.amtech.projectflow.model.project.ProjectStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ProjectDto {
    private UUID id;
    private String name;
    private String description;
    private Instant createDate;
    private ProjectStatus projectStatus;
    private LeadDto lead;
    private DirectionDto direction;

    @Data
    @Accessors(chain = true)
    public static class LeadDto {
        private UUID id;
        private String name;
    }

    @Data
    @Accessors(chain = true)
    public static class DirectionDto {
        private UUID id;
        private String name;
    }
}
