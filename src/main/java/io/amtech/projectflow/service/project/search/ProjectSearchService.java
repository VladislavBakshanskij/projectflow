package io.amtech.projectflow.service.project.search;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.dto.response.project.ProjectDto;
import io.amtech.projectflow.model.employee.UserPosition;

public interface ProjectSearchService {
    UserPosition searchType();

    PagedData<ProjectDto> search(SearchCriteria searchCriteria);
}
