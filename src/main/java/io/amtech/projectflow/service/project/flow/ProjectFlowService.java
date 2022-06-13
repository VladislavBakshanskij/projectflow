package io.amtech.projectflow.service.project.flow;

import io.amtech.projectflow.dto.response.project.flow.ProjectFlowResponse;

import java.util.UUID;

public interface ProjectFlowService {
    ProjectFlowResponse changeStatus(UUID projectId, String token);

    ProjectFlowResponse rollbackStatus(UUID projectId, String token);
}
