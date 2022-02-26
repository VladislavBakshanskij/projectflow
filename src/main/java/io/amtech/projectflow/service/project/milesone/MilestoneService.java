package io.amtech.projectflow.service.project.milesone;

import io.amtech.projectflow.dto.request.project.milestone.MilestoneCreateDto;
import io.amtech.projectflow.dto.request.project.milestone.MilestoneUpdateDto;
import io.amtech.projectflow.dto.request.project.milestone.MilestoneUpdateProgressDto;
import io.amtech.projectflow.dto.response.project.milestone.MilestoneDto;

import java.util.UUID;

public interface MilestoneService {
    MilestoneDto get(UUID projectId, UUID milestoneId);

    MilestoneDto create(UUID projectId, MilestoneCreateDto dto);

    void update(UUID projectId, UUID milestoneId, MilestoneUpdateDto dto);

    void delete(final UUID projectId, final UUID milestoneId);

    void updateProgress(UUID projectId, UUID milestoneId, MilestoneUpdateProgressDto dto);
}
