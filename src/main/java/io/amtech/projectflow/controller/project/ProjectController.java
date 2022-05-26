package io.amtech.projectflow.controller.project;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.app.SearchCriteriaBuilder;
import io.amtech.projectflow.dto.request.project.ProjectCreateDto;
import io.amtech.projectflow.dto.request.project.ProjectUpdateDto;
import io.amtech.projectflow.dto.response.project.ProjectDetailDto;
import io.amtech.projectflow.dto.response.project.ProjectSavedDto;
import io.amtech.projectflow.dto.response.project.ProjectDto;
import io.amtech.projectflow.service.project.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static io.amtech.projectflow.util.SearchUtil.FROM_DATE_KEY;
import static io.amtech.projectflow.util.SearchUtil.TO_DATE_KEY;

@RestController
@RequestMapping("projects")
@RequiredArgsConstructor
public class ProjectController {
    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectSavedDto create(@RequestBody @Valid ProjectCreateDto dto) {
        return projectService.create(dto);
    }

    @GetMapping("{id}")
    public ProjectDto get(@PathVariable UUID id) {
        return projectService.get(id);
    }

    @GetMapping("detail/{id}")
    public ProjectDetailDto getDetail(@PathVariable UUID id) {
        return projectService.getDetail(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable UUID id, @RequestBody @Valid ProjectUpdateDto dto) {
        projectService.update(id, dto);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        projectService.delete(id);
    }

    @GetMapping
    public PagedData<ProjectDto> search(@RequestParam(required = false, defaultValue = "100") Integer limit,
                                        @RequestParam(required = false, defaultValue = "0") Integer offset,
                                        @RequestParam(required = false, defaultValue = "name") String orders,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) UUID projectLeadId,
                                        @RequestParam(required = false) UUID directionId,
                                        @RequestParam(required = false) Instant createDateFrom,
                                        @RequestParam(required = false) Instant createDateTo,
                                        @RequestParam(required = false) String status) {
        final SearchCriteria criteria = new SearchCriteriaBuilder()
                .limit(limit)
                .offset(offset)
                .filter("name", name)
                .filter("project_lead", Optional.ofNullable(projectLeadId).map(Object::toString).orElse(null))
                .filter("direction", Optional.ofNullable(directionId).map(Object::toString).orElse(null))
                .filter("create_date_" + FROM_DATE_KEY, Optional.ofNullable(createDateFrom).map(Object::toString).orElse(null))
                .filter("create_date_" + TO_DATE_KEY, Optional.ofNullable(createDateTo).map(Object::toString).orElse(null))
                .filter("project_status", status)
                .order(orders)
                .build();

        return projectService.search(criteria);
    }
}
