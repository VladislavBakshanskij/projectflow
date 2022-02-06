package io.amtech.projectflow.service.project;

import io.amtech.projectflow.dto.request.project.ProjectCreateDto;
import io.amtech.projectflow.dto.request.project.ProjectUpdateDto;
import io.amtech.projectflow.dto.response.project.ProjectDto;
import io.amtech.projectflow.model.Project;
import io.amtech.projectflow.repository.direction.DirectionRepository;
import io.amtech.projectflow.repository.employee.EmployeeRepository;
import io.amtech.projectflow.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository projectRepository;
    private final DirectionRepository directionRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public void delete(final UUID id) {
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
        employeeRepository.checkOnExists(dto.getProjectLeadId());
        directionRepository.checkOnExists(dto.getDirectionId());

        Project project = new Project()
                .setName(dto.getName())
                .setDescription(dto.getDescription())
                .setProjectLeadId(dto.getProjectLeadId())
                .setDirectionId(dto.getDirectionId());
        projectRepository.update(id, project);
    }

    @Override
    public ProjectDto create(final ProjectCreateDto dto) {
        employeeRepository.checkOnExists(dto.getProjectLeadId());
        directionRepository.checkOnExists(dto.getDirectionId());

        Project projectToSave = new Project()
                .setName(dto.getName())
                .setProjectLeadId(dto.getProjectLeadId())
                .setDirectionId(dto.getDirectionId())
                .setDescription(dto.getDescription());
        Project project = projectRepository.save(projectToSave);
        return new ProjectDto()
                .setId(project.getId())
                .setName(project.getName())
                .setDescription(project.getDescription())
                .setProjectStatus(project.getStatus())
                .setCreateDate(project.getCreateDate())
                .setProjectLeadId(project.getProjectLeadId())
                .setDirectionId(project.getDirectionId());
    }
}
