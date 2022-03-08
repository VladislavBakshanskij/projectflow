package io.amtech.projectflow.service.project;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.dto.request.project.ProjectCreateDto;
import io.amtech.projectflow.dto.request.project.ProjectUpdateDto;
import io.amtech.projectflow.dto.response.project.ProjectDto;
import io.amtech.projectflow.listener.event.JournalEvent;
import io.amtech.projectflow.model.project.Project;
import io.amtech.projectflow.repository.direction.DirectionRepository;
import io.amtech.projectflow.repository.employee.EmployeeRepository;
import io.amtech.projectflow.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static io.amtech.projectflow.listener.event.JournalEventType.PROJECT;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final DirectionRepository directionRepository;
    private final EmployeeRepository employeeRepository;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void delete(final UUID id) {
        projectRepository.checkOnExists(id);
        projectRepository.delete(id);
    }

    @Override
    public ProjectDto get(final UUID id) {
        final Project project = projectRepository.findById(id);
        return new ProjectDto()
                .setId(project.getId())
                .setName(project.getName())
                .setDescription(project.getDescription())
                .setProjectStatus(project.getStatus())
                .setCreateDate(project.getCreateDate())
                .setProjectLeadId(project.getProjectLeadId())
                .setDirectionId(project.getDirectionId());
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
    public ProjectDto create(final ProjectCreateDto dto) {
        employeeRepository.checkOnExists(dto.getProjectLeadId());
        directionRepository.checkOnExists(dto.getDirectionId());

        final Project projectToSave = new Project()
                .setName(dto.getName())
                .setProjectLeadId(dto.getProjectLeadId())
                .setDirectionId(dto.getDirectionId())
                .setDescription(dto.getDescription());
        final Project project = projectRepository.save(projectToSave);
        applicationEventPublisher.publishEvent(new JournalEvent<>(PROJECT, project));
        return new ProjectDto()
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
                        .setProjectLeadId(project.getProjectLeadId())
                        .setDirectionId(project.getDirectionId()));
    }
}
