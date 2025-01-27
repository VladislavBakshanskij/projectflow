package io.amtech.projectflow.repository.project.journal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.amtech.projectflow.error.DataNotFoundException;
import io.amtech.projectflow.error.ProcessingException;
import io.amtech.projectflow.model.project.journal.ProjectJournal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.JSONB;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static io.amtech.projectflow.jooq.tables.ProjectJournal.PROJECT_JOURNAL;

@Slf4j
@Repository
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class ProjectJournalRepositoryImpl implements ProjectJournalRepository {
    private final DSLContext dsl;
    private final ProjectJournalRecordMapper mapper;
    private final ObjectMapper objectMapper;

    @Override
    public ProjectJournal findByIdWithProjectId(final UUID projectId, final UUID id) {
        return dsl.selectFrom(PROJECT_JOURNAL)
                .where(PROJECT_JOURNAL.ID.eq(id).and(PROJECT_JOURNAL.PROJECT_ID.eq(projectId)))
                .fetchOptional()
                .map(mapper::map)
                .orElseThrow(() -> new DataNotFoundException("Не найдена запись в журнале к проекту"));
    }

    @Override
    public ProjectJournal save(ProjectJournal projectJournal) {
        try {
            final JSONB currentState = JSONB.jsonb(objectMapper.writeValueAsString(projectJournal.getCurrentState()));
            final OffsetDateTime updateDate = OffsetDateTime.ofInstant(projectJournal.getUpdateDate(), ZoneId.systemDefault());

            return dsl.insertInto(PROJECT_JOURNAL)
                    .set(PROJECT_JOURNAL.ID, UUID.randomUUID())
                    .set(PROJECT_JOURNAL.PROJECT_ID, projectJournal.getProjectId())
                    .set(PROJECT_JOURNAL.LOGIN, projectJournal.getLogin())
                    .set(PROJECT_JOURNAL.UPDATE_DATE, updateDate)
                    .set(PROJECT_JOURNAL.CURRENT_STATE, currentState)
                    .returning()
                    .fetchOne()
                    .map(mapper);
        } catch (JsonProcessingException e) {
            log.error("Error on processing journal :: {}", projectJournal);
            throw new ProcessingException("Не удалось обработать запись в журнале");
        }
    }

    @Override
    public List<ProjectJournal> findByProjectId(final UUID projectId) {
        return dsl.selectFrom(PROJECT_JOURNAL)
                .where(PROJECT_JOURNAL.PROJECT_ID.eq(projectId))
                .orderBy(PROJECT_JOURNAL.UPDATE_DATE.desc())
                .fetch()
                .map(mapper);
    }

    @Override
    public boolean existsByProject(final UUID projectId) {
        return dsl.fetchExists(PROJECT_JOURNAL, PROJECT_JOURNAL.PROJECT_ID.eq(projectId));
    }
}
