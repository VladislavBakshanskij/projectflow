package io.amtech.projectflow.service.project.search;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.dto.response.project.ProjectDto;
import io.amtech.projectflow.model.employee.UserPosition;
import io.amtech.projectflow.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static io.amtech.projectflow.model.employee.UserPosition.PROJECT_LEAD;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectLeadProjectSearchServiceImpl extends AbstractProjectSearchService implements ProjectSearchService {
    private final ProjectRepository projectRepository;

    @Override
    public UserPosition searchType() {
        return PROJECT_LEAD;
    }

    @Override
    public PagedData<ProjectDto> search(SearchCriteria searchCriteria) {
        return projectRepository.searchForPosition(searchCriteria).map(getMapperToDto());
    }
}
