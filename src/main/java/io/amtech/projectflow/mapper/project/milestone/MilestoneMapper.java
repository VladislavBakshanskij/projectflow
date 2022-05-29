package io.amtech.projectflow.mapper.project.milestone;

import io.amtech.projectflow.dto.response.project.milestone.MilestoneDto;
import io.amtech.projectflow.model.project.milestone.Milestone;

import java.util.function.Function;

public interface MilestoneMapper extends Function<Milestone, MilestoneDto> {
}
