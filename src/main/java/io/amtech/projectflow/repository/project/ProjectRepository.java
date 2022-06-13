package io.amtech.projectflow.repository.project;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.model.project.Project;
import io.amtech.projectflow.model.project.ProjectStatus;
import io.amtech.projectflow.model.project.ProjectWithEmployeeDirection;

import java.util.UUID;

public interface ProjectRepository {
    void delete(UUID id);

    void checkOnExists(UUID id);

    ProjectWithEmployeeDirection findById(UUID id);

    void update(UUID id, Project project);

    void updateStatus(UUID id, ProjectStatus status);

    Project save(Project project);

    PagedData<ProjectWithEmployeeDirection> search(SearchCriteria criteria);

    PagedData<ProjectWithEmployeeDirection> searchForDirector(SearchCriteria searchCriteria);

    PagedData<ProjectWithEmployeeDirection> searchForPosition(SearchCriteria searchCriteria);
}
