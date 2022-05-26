package io.amtech.projectflow.service.project;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.dto.request.project.ProjectCreateDto;
import io.amtech.projectflow.dto.request.project.ProjectUpdateDto;
import io.amtech.projectflow.dto.response.project.*;
import io.amtech.projectflow.listener.event.JournalEvent;
import io.amtech.projectflow.model.project.Project;
import io.amtech.projectflow.model.project.ProjectWithEmployeeDirection;
import io.amtech.projectflow.model.project.journal.ProjectJournal;
import io.amtech.projectflow.model.project.milestone.Milestone;
import io.amtech.projectflow.repository.direction.DirectionRepository;
import io.amtech.projectflow.repository.employee.EmployeeRepository;
import io.amtech.projectflow.repository.project.ProjectRepository;
import io.amtech.projectflow.repository.project.journal.ProjectJournalRepository;
import io.amtech.projectflow.repository.project.milesone.MilestoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static io.amtech.projectflow.listener.event.JournalEventType.PROJECT;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final EmployeeRepository employeeRepository;
    private final DirectionRepository directionRepository;
    private final MilestoneRepository milestoneRepository;
    private final ProjectJournalRepository projectJournalRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void delete(final UUID id) {
        projectRepository.checkOnExists(id);
        projectRepository.delete(id);
    }

    @Override
    public ProjectDto get(final UUID id) {
        final ProjectWithEmployeeDirection project = projectRepository.findById(id);
        return new ProjectDto()
                .setId(project.getId())
                .setName(project.getName())
                .setDescription(project.getDescription())
                .setProjectStatus(project.getStatus())
                .setCreateDate(project.getCreateDate())
                .setLead(new LeadDto()
                                 .setId(project.getLead().getId())
                                 .setName(project.getLead().getName()))
                .setDirection(new ProjectDirectionDto()
                                      .setId(project.getDirection().getId())
                                      .setName(project.getDirection().getName()));
    }

    @Override
    public void update(final UUID id, final ProjectUpdateDto dto) {
        projectRepository.checkOnExists(id);
        employeeRepository.checkOnExists(dto.getProjectLeadId());
        directionRepository.checkOnExists(dto.getDirectionId());

        final Project project = new Project()
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setProjectLeadId(dto.getProjectLeadId())
                .setDirectionId(dto.getDirectionId());
        projectRepository.update(id, project);
        applicationEventPublisher.publishEvent(new JournalEvent<>(PROJECT, project
                .setId(id)));
    }

    @Override
    public ProjectSavedDto create(final ProjectCreateDto dto) {
        employeeRepository.checkOnExists(dto.getProjectLeadId());
        directionRepository.checkOnExists(dto.getDirectionId());

        final Project projectToSave = new Project()
                .setName(dto.getName())
                .setProjectLeadId(dto.getProjectLeadId())
                .setDirectionId(dto.getDirectionId())
                .setDescription(dto.getDescription());
        final Project project = projectRepository.save(projectToSave);
        applicationEventPublisher.publishEvent(new JournalEvent<>(PROJECT, project));
        return new ProjectSavedDto()
                .setId(project.getId())
                .setName(project.getName())
                .setDescription(project.getDescription())
                .setProjectStatus(project.getStatus())
                .setCreateDate(project.getCreateDate())
                .setProjectLeadId(project.getProjectLeadId())
                .setDirectionId(project.getDirectionId());
    }

    @Override
    public PagedData<ProjectDto> search(final SearchCriteria criteria) {
        return projectRepository.search(criteria)
                .map(project -> new ProjectDto()
                        .setId(project.getId())
                        .setName(project.getName())
                        .setDescription(project.getDescription())
                        .setProjectStatus(project.getStatus())
                        .setCreateDate(project.getCreateDate())
                        .setLead(new LeadDto()
                                         .setId(project.getLead().getId())
                                         .setName(project.getLead().getName()))
                        .setDirection(new ProjectDirectionDto()
                                              .setId(project.getDirection().getId())
                                              .setName(project.getDirection().getName())));
    }

    @Override
    public ProjectDetailDto getDetail(final UUID id) {
        final ProjectWithEmployeeDirection project = projectRepository.findById(id);
        final List<Milestone> milestones = milestoneRepository.findByProjectId(id);
        final List<ProjectJournal> projectJournals = projectJournalRepository.findByProjectId(id);
        return new ProjectDetailDto()
                .setId(project.getId())
                .setName(project.getName())
                .setStatus(project.getStatus())
                .setCreateDate(project.getCreateDate())
                .setDescription(project.getDescription())
                .setLead(new LeadDto()
                                 .setId(project.getLead().getId())
                                 .setName(project.getLead().getName()))
                .setDirection(new ProjectDirectionDto()
                                      .setId(project.getDirection().getId())
                                      .setName(project.getDirection().getName()))
                .setMilestones(buildShortMilestoneDto(milestones))
                .setHistory(buildHistory(projectJournals));
    }

    private List<ProjectDetailDto.HistoryItemDto> buildHistory(final List<ProjectJournal> projectJournals) {
        return projectJournals.stream()
                .map(projectJournal -> new ProjectDetailDto.HistoryItemDto()
                        .setId(projectJournal.getId())
                        .setLogin(projectJournal.getLogin())
                        .setUpdateDate(projectJournal.getUpdateDate()))
                .collect(Collectors.toList());
    }

    private List<ProjectDetailDto.ShortMilestoneDto> buildShortMilestoneDto(final List<Milestone> milestones) {
        return milestones
                .stream()
                .map(milestone -> new ProjectDetailDto.ShortMilestoneDto()
                        .setId(milestone.getId())
                        .setName(milestone.getName())
                        .setDescription(milestone.getDescription())
                        .setProgress(milestone.getProgressPercent()))
                .collect(Collectors.toList());
    }
}
