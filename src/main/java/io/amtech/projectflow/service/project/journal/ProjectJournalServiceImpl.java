package io.amtech.projectflow.service.project.journal;

import io.amtech.projectflow.dto.response.project.journal.ProjectJournalDto;
import io.amtech.projectflow.dto.response.project.journal.ShortProjectJournalDto;
import io.amtech.projectflow.model.project.journal.ProjectJournal;
import io.amtech.projectflow.repository.project.ProjectRepository;
import io.amtech.projectflow.repository.project.journal.ProjectJournalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ProjectJournalServiceImpl implements ProjectJournalService {
    private final ProjectRepository projectRepository;
    private final ProjectJournalRepository projectJournalRepository;

    @Override
    public ProjectJournalDto get(final UUID projectId, final UUID id) {
        projectRepository.checkOnExists(projectId);
        final ProjectJournal journal = projectJournalRepository.findByIdWithProjectId(projectId, id);
        return new ProjectJournalDto()
                .setId(journal.getId())
                .setProjectId(journal.getProjectId())
                .setUpdateDate(journal.getUpdateDate())
                .setLogin(journal.getLogin())
                .setCurrentState(journal.getCurrentState());
    }

    @Override
    public List<ShortProjectJournalDto> getAllByProject(final UUID projectId) {
        projectRepository.checkOnExists(projectId);
        final List<ProjectJournal> history = projectJournalRepository.findByProjectId(projectId);
        return history.stream()
                .map(journal -> new ShortProjectJournalDto()
                        .setId(journal.getId())
                        .setProjectId(journal.getProjectId())
                        .setLogin(journal.getLogin())
                        .setUpdateDate(journal.getUpdateDate())
                )
                .collect(Collectors.toList());
    }
}
