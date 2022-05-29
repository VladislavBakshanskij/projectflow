package io.amtech.projectflow.mapper.project.milestone;

import io.amtech.projectflow.dto.response.project.milestone.MilestoneDto;
import io.amtech.projectflow.model.project.milestone.Milestone;
import org.springframework.stereotype.Component;

@Component
public class MilestoneMapperImpl implements MilestoneMapper {
    @Override
    public MilestoneDto apply(final Milestone milestone) {
        return new MilestoneDto()
                .setId(milestone.getId())
                .setName(milestone.getName())
                .setDescription(milestone.getDescription())
                .setPlannedStartDate(milestone.getPlannedStartDate())
                .setPlannedFinishDate(milestone.getPlannedFinishDate())
                .setFactStartDate(milestone.getFactStartDate())
                .setFactFinishDate(milestone.getFactFinishDate())
                .setProgressPercent(milestone.getProgressPercent());
    }
}
