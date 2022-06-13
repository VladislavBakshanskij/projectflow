package io.amtech.projectflow.service.project.flow.strategy;

import io.amtech.projectflow.model.employee.UserPosition;
import io.amtech.projectflow.model.project.ProjectStatus;

import java.util.UUID;

public interface FlowStrategy {
    ProjectStatus supportStatus();

    void validatePosition(UserPosition userPosition);

    void changeStatus(UUID projectId);

    void rollbackStatus(UUID projectId);
}
