package io.amtech.projectflow.service.project;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.dto.request.project.ProjectCreateDto;
import io.amtech.projectflow.dto.request.project.ProjectUpdateDto;
import io.amtech.projectflow.dto.response.project.ProjectDetailDto;
import io.amtech.projectflow.dto.response.project.ProjectDto;
import io.amtech.projectflow.dto.response.project.ProjectSavedDto;

import java.util.UUID;

public interface ProjectService {
    void delete(UUID id);

    ProjectDto get(UUID id);

    void update(UUID id, ProjectUpdateDto dto);

    ProjectSavedDto create(ProjectCreateDto dto);

    PagedData<ProjectDto> search(SearchCriteria criteria);

    ProjectDetailDto getDetail(UUID id);
}
