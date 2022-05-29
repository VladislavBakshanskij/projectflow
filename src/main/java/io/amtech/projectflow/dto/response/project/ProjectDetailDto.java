package io.amtech.projectflow.dto.response.project;

import io.amtech.projectflow.dto.response.project.milestone.MilestoneDto;
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
    private List<MilestoneDto> milestones = new ArrayList<>();
    private List<CommentDto> comments = new ArrayList<>();

    @Data
    @Accessors(chain = true)
    public static class CommentDto {
        private String message;
        private String login;
        private Instant createDate;
    }
}
