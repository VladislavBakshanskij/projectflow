package io.amtech.projectflow.service.project.flow.strategy;

import io.amtech.projectflow.model.employee.UserPosition;
import io.amtech.projectflow.model.project.ProjectStatus;
import io.amtech.projectflow.repository.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OnDirectorApprovingFlowStrategy extends AbstractFlowStrategy implements FlowStrategy {
    private final ProjectRepository projectRepository;

    @Override
    public ProjectStatus supportStatus() {
        return ProjectStatus.ON_DIRECTOR_APPROVING;
    }

    @Override
    public ProjectStatus changeStatus(final UUID projectId) {
        projectRepository.updateStatus(projectId, ProjectStatus.DIRECTOR_APPROVED);
        return ProjectStatus.DIRECTOR_APPROVED;
    }

    @Override
    public ProjectStatus rollbackStatus(final UUID projectId) {
        projectRepository.updateStatus(projectId, ProjectStatus.ON_DL_APPROVING);
        return ProjectStatus.ON_DL_APPROVING;
    }

    @Override
    protected UserPosition getSupportPosition() {
        return UserPosition.DIRECTOR;
    }
}
