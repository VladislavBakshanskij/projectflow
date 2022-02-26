package io.amtech.projectflow.service.project.milesone;

import io.amtech.projectflow.dto.request.project.milestone.MilestoneCreateDto;
import io.amtech.projectflow.dto.request.project.milestone.MilestoneUpdateDto;
import io.amtech.projectflow.dto.request.project.milestone.MilestoneUpdateProgressDto;
import io.amtech.projectflow.dto.response.project.milestone.MilestoneDto;
import io.amtech.projectflow.model.project.milestone.Milestone;
import io.amtech.projectflow.repository.project.ProjectRepository;
import io.amtech.projectflow.repository.project.milesone.MilestoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MilestoneServiceImpl implements MilestoneService {
    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;

    @Override
    public MilestoneDto get(final UUID projectId, final UUID milestoneId) {
        projectRepository.checkOnExists(projectId);
        final Milestone milestone = milestoneRepository.findByIdWithProject(projectId, milestoneId);
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

    @Override
    public MilestoneDto create(final UUID projectId, final MilestoneCreateDto dto) {
        projectRepository.checkOnExists(projectId);
        final Milestone milestoneToSave = new Milestone()
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setProjectId(projectId)
                .setPlannedStartDate(dto.getPlannedStartDate())
                .setPlannedFinishDate(dto.getPlannedFinishDate())
                .setFactStartDate(dto.getFactStartDate())
                .setFactFinishDate(dto.getFactFinishDate())
                .setProgressPercent(dto.getProgressPercent());
        final Milestone saved = milestoneRepository.save(milestoneToSave);
        return new MilestoneDto()
                .setId(saved.getId())
                .setName(saved.getName())
                .setDescription(saved.getDescription())
                .setPlannedStartDate(saved.getPlannedStartDate())
                .setPlannedFinishDate(saved.getPlannedFinishDate())
                .setFactStartDate(saved.getFactStartDate())
                .setFactFinishDate(saved.getFactFinishDate())
                .setProgressPercent(saved.getProgressPercent());
    }

    @Override
    public void update(final UUID projectId, final UUID milestoneId, final MilestoneUpdateDto dto) {
        projectRepository.checkOnExists(projectId);
        milestoneRepository.checkOnExistsWithProject(projectId, milestoneId);
        Milestone milestone = new Milestone()
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setProjectId(projectId)
                .setPlannedStartDate(dto.getPlannedStartDate())
                .setPlannedFinishDate(dto.getPlannedFinishDate())
                .setFactStartDate(dto.getFactStartDate())
                .setFactFinishDate(dto.getFactFinishDate())
                .setProgressPercent(dto.getProgressPercent());
        milestoneRepository.update(milestoneId, milestone);
    }

    @Override
    public void delete(final UUID projectId, final UUID milestoneId) {
        projectRepository.checkOnExists(projectId);
        milestoneRepository.checkOnExistsWithProject(projectId, milestoneId);
        milestoneRepository.delete(milestoneId);
    }

    @Override
    public void updateProgress(final UUID projectId, final UUID milestoneId, final MilestoneUpdateProgressDto dto) {
        projectRepository.checkOnExists(projectId);
        milestoneRepository.checkOnExistsWithProject(projectId, milestoneId);
        milestoneRepository.updateProgress(milestoneId, dto.getProgress());
    }
}
