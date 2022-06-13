package io.amtech.projectflow.controller.project.flow;

import io.amtech.projectflow.dto.response.project.flow.ProjectFlowResponse;
import io.amtech.projectflow.service.project.flow.ProjectFlowService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("projects/{projectId}/flow")
@RequiredArgsConstructor
public class ProjectFlowController {
    private final ProjectFlowService projectFlowService;

    @PatchMapping
    public ProjectFlowResponse changeStatus(@PathVariable UUID projectId,
                                            @ApiIgnore @AuthenticationPrincipal String token) {
        return projectFlowService.changeStatus(projectId, token);
    }

    @PatchMapping("rollback")
    public ProjectFlowResponse rollbackStatus(@PathVariable UUID projectId,
                                              @ApiIgnore @AuthenticationPrincipal String token) {
        return projectFlowService.rollbackStatus(projectId, token);
    }
}
