package io.amtech.projectflow.service.project.flow.strategy;

import io.amtech.projectflow.error.FinalStatusException;
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
public class UnapprovedFlowStrategy extends AbstractFlowStrategy implements FlowStrategy {
    private final ProjectRepository projectRepository;

    @Override
    public ProjectStatus supportStatus() {
        return ProjectStatus.UNAPPROVED;
    }

    @Override
    public void changeStatus(final UUID projectId) {
        projectRepository.updateStatus(projectId, ProjectStatus.ON_PL_PLANNING);
    }

    @Override
    public void rollbackStatus(final UUID projectId) {
        throw new FinalStatusException("Статус проекта находится в финальтом статусе и дальше не может быть изменен");
    }

    @Override
    protected UserPosition getSupportPosition() {
        return UserPosition.PROJECT_LEAD;
    }
}
