package io.amtech.projectflow.controller.project;

import io.amtech.projectflow.dto.request.project.ProjectCreateDto;
import io.amtech.projectflow.dto.request.project.ProjectUpdateDto;
import io.amtech.projectflow.dto.response.project.ProjectDto;
import io.amtech.projectflow.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    public ProjectDto create(@RequestBody @Valid ProjectCreateDto dto) {
        return projectService.create(dto);
    }

    @PutMapping("{id}")
    public void update(@PathVariable UUID id, @RequestBody @Valid ProjectUpdateDto dto) {
        projectService.update(id, dto);
    }

    @GetMapping("{id}")
    public ProjectDto get(@PathVariable UUID id) {
        return projectService.get(id);
    }

    @DeleteMapping("{id}")
    public void delete(@PathVariable UUID id) {
        projectService.delete(id);
    }
}
