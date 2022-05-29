package io.amtech.projectflow.listener.event;

import io.amtech.projectflow.model.project.ProjectComment;
import io.amtech.projectflow.model.project.ProjectWithEmployeeDirection;
import io.amtech.projectflow.model.project.milestone.Milestone;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ProjectJournalData implements JournalEventData {
    private ProjectWithEmployeeDirection project;
    private List<Milestone> milestones = new ArrayList<>();
    private List<ProjectComment> comments = new ArrayList<>();
}
