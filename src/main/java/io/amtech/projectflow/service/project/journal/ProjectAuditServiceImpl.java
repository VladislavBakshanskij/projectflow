package io.amtech.projectflow.service.project.journal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.amtech.projectflow.dto.request.project.ProjectJournalEvent;
import io.amtech.projectflow.error.ProcessingException;
import io.amtech.projectflow.listener.event.JournalEventType;
import io.amtech.projectflow.listener.event.ProjectJournalData;
import io.amtech.projectflow.model.auth.Token;
import io.amtech.projectflow.model.project.ProjectWithEmployeeDirection;
import io.amtech.projectflow.model.project.journal.EventType;
import io.amtech.projectflow.model.project.journal.ProjectJournal;
import io.amtech.projectflow.publisher.project.ProjectEventPublisher;
import io.amtech.projectflow.repository.direction.DirectionRepository;
import io.amtech.projectflow.repository.employee.EmployeeRepository;
import io.amtech.projectflow.repository.project.journal.ProjectJournalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

import static io.amtech.projectflow.listener.event.JournalEventType.PROJECT;
import static io.amtech.projectflow.model.project.journal.EventType.CREATE;
import static io.amtech.projectflow.model.project.journal.EventType.UPDATE;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectAuditServiceImpl implements AuditService<ProjectJournalData> {
    private final ProjectJournalRepository projectJournalRepository;
    private final ProjectEventPublisher projectEventPublisher;
    private final DirectionRepository directionRepository;
    private final EmployeeRepository employeeRepository;
    private final ObjectMapper objectMapper;

    @Override
    public JournalEventType getSupportType() {
        return PROJECT;
    }

    @Override
    public void save(final ProjectJournalData data) {
        try {
            log.debug("Start write data into journal for project :: {}", data);
            final Token token = (Token) SecurityContextHolder.getContext().getAuthentication().getCredentials();
            final String login = token.getUser()
                    .getUser()
                    .getLogin();
            ProjectWithEmployeeDirection project = data.getProject();
            final UUID projectId = project.getId();
            final EventType eventType = projectJournalRepository.existsByProject(projectId)
                    ? UPDATE
                    : CREATE;
            final ProjectJournal projectJournalToSave = new ProjectJournal()
                    .setProjectId(project.getId())
                    .setLogin(login)
                    .setUpdateDate(Instant.now())
                    .setCurrentState(objectMapper.convertValue(data, new TypeReference<>() {
                    }));
            final ProjectJournal saved = projectJournalRepository.save(projectJournalToSave);

            log.debug("Save data for project changes into journal :: {}", saved);
            final ProjectJournalEvent.Event projectLeadNotification = new ProjectJournalEvent.Event()
                    .setProjectName(project.getName())
                    .setChanges(saved.getCurrentState())
                    .setLogin(login)
                    .setSubject(eventType.name())
                    .setRecipient(employeeRepository.findById(project.getLead().getId()).getEmail());
            projectEventPublisher.notifyOnChanges(new ProjectJournalEvent()
                                                          .setEvent(projectLeadNotification)
                                                          .setEventType(eventType));

            final String leadEmail = directionRepository.findLeadEmail(project.getDirection().getId());
            final ProjectJournalEvent.Event directionLeadNotification = new ProjectJournalEvent.Event()
                    .setProjectName(project.getName())
                    .setChanges(saved.getCurrentState())
                    .setLogin(login)
                    .setSubject(eventType.name())
                    .setRecipient(leadEmail);
            projectEventPublisher.notifyOnChanges(new ProjectJournalEvent()
                                                          .setEvent(directionLeadNotification)
                                                          .setEventType(eventType));
        } catch (IllegalArgumentException e) {
            log.error("Error on processing project({})", data, e);
            throw new ProcessingException("Не удалось сохранить данные в журнал");
        }
    }
}
