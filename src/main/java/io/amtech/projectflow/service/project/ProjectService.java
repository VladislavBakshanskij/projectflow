package io.amtech.projectflow.service.project;

import io.amtech.projectflow.dto.request.project.ProjectCreateDto;
import io.amtech.projectflow.dto.request.project.ProjectUpdateDto;
import io.amtech.projectflow.dto.response.project.ProjectDto;

import java.util.UUID;

public interface ProjectService {
    void delete(UUID id);

    ProjectDto get(UUID id);

    void update(UUID id, ProjectUpdateDto dto);

    ProjectDto create(ProjectCreateDto dto);
}
