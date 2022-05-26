package io.amtech.projectflow.dto.response.project;

import io.amtech.projectflow.model.project.ProjectStatus;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Accessors(chain = true)
public class ProjectDetailDto {
    private UUID id;
    private String name;
    private String description;
    private Instant createDate;
    private ProjectStatus status;
    private LeadDto lead;
    private ProjectDirectionDto direction;
    private List<ShortMilestoneDto> milestones = new ArrayList<>();
    private List<HistoryItemDto> history = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    public static class ShortMilestoneDto {
        private UUID id;
        private String name;
        private String description;
        private short progress;
    }

    @Data
    @Accessors(chain = true)
    public static class HistoryItemDto {
        private UUID id;
        private String login;
        private Instant updateDate;
    }
}
