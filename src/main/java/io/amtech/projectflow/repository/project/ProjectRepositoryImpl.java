package io.amtech.projectflow.repository.project;

import io.amtech.projectflow.error.DataNotFoundException;
import io.amtech.projectflow.model.project.Project;
import io.amtech.projectflow.model.project.ProjectStatus;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;

import static io.amtech.projectflow.jooq.tables.Project.PROJECT;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements ProjectRepository {
    public static final RecordMapper<Record, Project> mapper = record -> new Project()
            .setId(record.get(PROJECT.ID))
            .setName(record.get(PROJECT.NAME))
            .setDescription(record.get(PROJECT.DESCRIPTION))
            .setStatus(record.get(PROJECT.STATUS))
            .setProjectLeadId(record.get(PROJECT.PROJECT_LEAD_ID))
            .setDirectionId(record.get(PROJECT.DIRECTION_ID))
            .setCreateDate(Instant.from(record.get(PROJECT.CREATE_DATE)));

    private final DSLContext dsl;

    @Override
    public void delete(final UUID id) {
        dsl.deleteFrom(PROJECT)
                .where(PROJECT.ID.eq(id))
                .execute();
    }

    @Override
    public void checkOnExists(UUID id) {
        final boolean exists = dsl.fetchExists(PROJECT, PROJECT.ID.eq(id));
        if (!exists) {
            throw new DataNotFoundException("Проект не найден");
        }
    }

    @Override
    public Project findById(final UUID id) {
        return dsl.selectFrom(PROJECT)
                .where(PROJECT.ID.eq(id))
                .fetchOptional()
                .map(mapper::map)
                .orElseThrow(() -> new DataNotFoundException("Проект " + id + " не найден"));
    }

    @Override
    public void update(final UUID id, final Project project) {
        dsl.update(PROJECT)
                .set(PROJECT.NAME, project.getName())
                .set(PROJECT.PROJECT_LEAD_ID, project.getProjectLeadId())
                .set(PROJECT.DIRECTION_ID, project.getDirectionId())
                .set(PROJECT.DESCRIPTION, project.getDescription())
                .where(PROJECT.ID.eq(id))
                .execute();
    }

    @Override
    public Project save(final Project project) {
        return dsl.insertInto(PROJECT)
                .set(PROJECT.ID, UUID.randomUUID())
                .set(PROJECT.NAME, project.getName())
                .set(PROJECT.STATUS, ProjectStatus.UNAPPROVED)
                .set(PROJECT.CREATE_DATE, OffsetDateTime.now())
                .set(PROJECT.PROJECT_LEAD_ID, project.getProjectLeadId())
                .set(PROJECT.DIRECTION_ID, project.getDirectionId())
                .set(PROJECT.DESCRIPTION, project.getDescription())
                .returning()
                .fetchOne()
                .map(mapper);
    }
}
