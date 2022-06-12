package io.amtech.projectflow.service.project.search;

import io.amtech.projectflow.dto.response.project.ProjectDto;
import io.amtech.projectflow.model.project.ProjectWithEmployeeDirection;
import io.amtech.projectflow.service.project.mapper.ProjectMapper;

import java.util.function.Function;

public abstract class AbstractProjectSearchService implements ProjectSearchService {
    protected Function<ProjectWithEmployeeDirection, ProjectDto> getMapperToDto() {
        return ProjectMapper::toDto;
    }
}
