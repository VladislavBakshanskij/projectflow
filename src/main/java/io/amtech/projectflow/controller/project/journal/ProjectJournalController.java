package io.amtech.projectflow.controller.project.journal;

import io.amtech.projectflow.dto.response.project.journal.ProjectJournalDto;
import io.amtech.projectflow.dto.response.project.journal.ShortProjectJournalDto;
import io.amtech.projectflow.service.project.journal.ProjectJournalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("projects/{projectId}/journals")
@RequiredArgsConstructor
public class ProjectJournalController {
    private final ProjectJournalService projectJournalService;

    @GetMapping
    public List<ShortProjectJournalDto> getAllByProject(@PathVariable UUID projectId) {
        return projectJournalService.getAllByProject(projectId);
    }

    @GetMapping("{id}")
    public ProjectJournalDto get(@PathVariable UUID projectId, @PathVariable UUID id) {
        return projectJournalService.get(projectId, id);
    }
}
