package io.amtech.projectflow.model.project;

import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ProjectWithEmployeeDirection {
    private UUID id;
    private String name;
    private Instant createDate;
    private ProjectStatus status;
    private String description;
    private Lead lead;
    private Direction direction;

    @Data
    @Accessors(chain = true)
    public static class Lead {
        private UUID id;
        private String name;
    }

    @Data
    @Accessors(chain = true)
    public static class Direction {
        private UUID id;
        private String name;
    }
}
