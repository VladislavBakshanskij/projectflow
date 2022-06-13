package io.amtech.projectflow.service.project.flow;

import io.amtech.projectflow.dto.response.project.flow.ProjectFlowResponse;
import io.amtech.projectflow.model.employee.UserPosition;
import io.amtech.projectflow.model.project.ProjectStatus;
import io.amtech.projectflow.model.project.ProjectWithEmployeeDirection;
import io.amtech.projectflow.repository.project.ProjectRepository;
import io.amtech.projectflow.service.project.flow.strategy.FlowStrategy;
import io.amtech.projectflow.service.token.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
public class ProjectFlowServiceImpl implements ProjectFlowService {
    private final TokenService tokenService;
    private final ProjectRepository projectRepository;
    private final Map<ProjectStatus, FlowStrategy> flowStrategies;

    public ProjectFlowServiceImpl(final TokenService tokenService,
                                  final ProjectRepository projectRepository,
                                  final List<FlowStrategy> flowStrategies) {
        this.tokenService = tokenService;
        this.projectRepository = projectRepository;
        this.flowStrategies = flowStrategies.stream()
                .collect(Collectors.toMap(FlowStrategy::supportStatus, Function.identity()));
    }

    @Override
    public ProjectFlowResponse changeStatus(final UUID projectId, final String token) {
        final UserPosition userPosition = tokenService.getByAccess(token)
                .getUser()
                .getEmployee()
                .getUserPosition();

        final ProjectWithEmployeeDirection project = projectRepository.findById(projectId);
        final FlowStrategy flowStrategy = flowStrategies.get(project.getStatus());
        flowStrategy.validatePosition(userPosition);
        flowStrategy.changeStatus(projectId);
        return new ProjectFlowResponse().setStatus(HttpStatus.OK.name());
    }

    @Override
    public ProjectFlowResponse rollbackStatus(final UUID projectId, final String token) {
        final UserPosition userPosition = tokenService.getByAccess(token)
                .getUser()
                .getEmployee()
                .getUserPosition();

        final ProjectWithEmployeeDirection project = projectRepository.findById(projectId);
        final FlowStrategy flowStrategy = flowStrategies.get(project.getStatus());
        flowStrategy.validatePosition(userPosition);
        flowStrategy.rollbackStatus(projectId);
        return new ProjectFlowResponse().setStatus(HttpStatus.OK.name());
    }
}
