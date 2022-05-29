package io.amtech.projectflow.controller.project.milestone;

import io.amtech.projectflow.dto.request.project.milestone.MilestoneCreateDto;
import io.amtech.projectflow.dto.request.project.milestone.MilestoneUpdateDto;
import io.amtech.projectflow.dto.request.project.milestone.MilestoneUpdateProgressDto;
import io.amtech.projectflow.dto.response.project.milestone.MilestoneDto;
import io.amtech.projectflow.dto.response.project.milestone.MilestoneUpdateResponseDto;
import io.amtech.projectflow.service.project.milesone.MilestoneService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("projects/{projectId}/milestones")
@RequiredArgsConstructor
public class MilestoneController {
    private final MilestoneService milestoneService;

    @GetMapping("{milestoneId}")
    public MilestoneDto get(@PathVariable UUID projectId, @PathVariable UUID milestoneId) {
        return milestoneService.get(projectId, milestoneId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MilestoneDto create(@PathVariable UUID projectId, @Valid @RequestBody MilestoneCreateDto dto) {
        return milestoneService.create(projectId, dto);
    }

    @PutMapping("{milestoneId}")
    public MilestoneUpdateResponseDto update(@PathVariable UUID projectId,
                                             @PathVariable UUID milestoneId,
                                             @Valid @RequestBody MilestoneUpdateDto dto) {
        return milestoneService.update(projectId, milestoneId, dto);
    }

    @DeleteMapping("{milestoneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMapping(@PathVariable UUID projectId, @PathVariable UUID milestoneId) {
        milestoneService.delete(projectId, milestoneId);
    }

    @PatchMapping("{milestoneId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProgress(@PathVariable UUID projectId,
                               @PathVariable UUID milestoneId,
                               @Valid @RequestBody MilestoneUpdateProgressDto dto) {
        milestoneService.updateProgress(projectId, milestoneId, dto);
    }
}
