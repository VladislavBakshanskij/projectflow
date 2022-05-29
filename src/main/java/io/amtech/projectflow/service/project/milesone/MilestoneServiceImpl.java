package io.amtech.projectflow.service.project.milesone;

import io.amtech.projectflow.dto.request.project.milestone.MilestoneCreateDto;
import io.amtech.projectflow.dto.request.project.milestone.MilestoneUpdateDto;
import io.amtech.projectflow.dto.request.project.milestone.MilestoneUpdateProgressDto;
import io.amtech.projectflow.dto.response.project.milestone.MilestoneDto;
import io.amtech.projectflow.dto.response.project.milestone.MilestoneUpdateResponseDto;
import io.amtech.projectflow.mapper.project.milestone.MilestoneMapper;
import io.amtech.projectflow.model.project.milestone.Milestone;
import io.amtech.projectflow.repository.project.ProjectRepository;
import io.amtech.projectflow.repository.project.milesone.MilestoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class MilestoneServiceImpl implements MilestoneService {
    private final MilestoneMapper milestoneMapper;
    private final MilestoneRepository milestoneRepository;
    private final ProjectRepository projectRepository;

    @Override
    public MilestoneDto get(final UUID projectId, final UUID milestoneId) {
        projectRepository.checkOnExists(projectId);
        final Milestone milestone = milestoneRepository.findByIdWithProject(projectId, milestoneId);
        return milestoneMapper.apply(milestone);
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
                .setProgressPercent(dto.getProgressPercent())
                .setCreateDate(Instant.now());
        final Milestone saved = milestoneRepository.save(milestoneToSave);
        return milestoneMapper.apply(saved);
    }

    @Override
    public MilestoneUpdateResponseDto update(final UUID projectId, final UUID milestoneId, final MilestoneUpdateDto dto) {
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
        return new MilestoneUpdateResponseDto()
                .setStatus(HttpStatus.OK.name());
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
