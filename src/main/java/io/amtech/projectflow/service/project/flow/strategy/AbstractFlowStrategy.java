package io.amtech.projectflow.service.project.flow.strategy;

import io.amtech.projectflow.error.ForbiddenException;
import io.amtech.projectflow.model.employee.UserPosition;

public abstract class AbstractFlowStrategy implements FlowStrategy {
    @Override
    public void validatePosition(UserPosition userPosition) {
        final UserPosition supportPosition = getSupportPosition();
        if (supportPosition != userPosition) {
            throw new ForbiddenException("Доступно только для пользователей с ролью " + supportPosition.name());
        }
    }

    protected abstract UserPosition getSupportPosition();
}
