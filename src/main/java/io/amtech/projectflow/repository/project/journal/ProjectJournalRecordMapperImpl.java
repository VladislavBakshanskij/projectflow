package io.amtech.projectflow.repository.project.journal;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.amtech.projectflow.error.ProcessingException;
import io.amtech.projectflow.model.project.journal.ProjectJournal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Record;
import org.springframework.stereotype.Component;

import static io.amtech.projectflow.jooq.tables.ProjectJournal.PROJECT_JOURNAL;

@Component
@Slf4j
@RequiredArgsConstructor
public class ProjectJournalRecordMapperImpl implements ProjectJournalRecordMapper {
    private final ObjectMapper objectMapper;

    @Override
    public ProjectJournal map(Record record) {
        try {
            return new ProjectJournal()
                    .setId(record.get(PROJECT_JOURNAL.ID))
                    .setProjectId(record.get(PROJECT_JOURNAL.PROJECT_ID))
                    .setUpdateDate(record.get(PROJECT_JOURNAL.UPDATE_DATE).toInstant())
                    .setLogin(record.get(PROJECT_JOURNAL.LOGIN))
                    .setCurrentState(objectMapper.readValue(record.get(PROJECT_JOURNAL.CURRENT_STATE).data(),
                            new TypeReference<>() {
                            }));
        } catch (JsonProcessingException e) {
            log.error("Error on processing project({}) and journal({})",
                    record.get(PROJECT_JOURNAL.PROJECT_ID), record.get(PROJECT_JOURNAL.ID));
            throw new ProcessingException("Не удалось обработать запись в журнале");
        }
    }
}
