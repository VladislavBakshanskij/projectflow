package io.amtech.projectflow.service.project.flow.strategy;

import io.amtech.projectflow.error.FinalStatusException;
import io.amtech.projectflow.model.employee.UserPosition;
import io.amtech.projectflow.model.project.ProjectStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
public class DoneFlowStrategy implements FlowStrategy {
    @Override
    public ProjectStatus supportStatus() {
        return ProjectStatus.DONE;
    }

    @Override
    public void validatePosition(UserPosition userPosition) {
        throw new FinalStatusException("Статус проекта не может быть изменен.");
    }

    @Override
    public void changeStatus(final UUID projectId) {
        throw new FinalStatusException("Статус проекта не может быть изменен.");
    }

    @Override
    public void rollbackStatus(final UUID projectId) {
        throw new FinalStatusException("Статус проекта не может быть изменен.");
    }
}
