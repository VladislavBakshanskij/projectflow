package io.amtech.projectflow.repository.project.milesone;

import io.amtech.projectflow.error.DataNotFoundException;
import io.amtech.projectflow.model.project.milestone.Milestone;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static io.amtech.projectflow.jooq.tables.Milestone.MILESTONE;

@Repository
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class MilestoneRepositoryImpl implements MilestoneRepository {
    public static final RecordMapper<Record, Milestone> mapper =
            record -> {
                final OffsetDateTime startDate = record.get(MILESTONE.FACT_START_DATE);
                final OffsetDateTime endDate = record.get(MILESTONE.FACT_FINISH_DATE);
                final Instant startDateInstant = Objects.isNull(startDate) ? null : startDate.toInstant();
                final Instant endDateInstant = Objects.isNull(endDate) ? null : endDate.toInstant();
                return new Milestone()
                        .setId(record.get(MILESTONE.ID))
                        .setProjectId(record.get(MILESTONE.PROJECT_ID))
                        .setName(record.get(MILESTONE.NAME))
                        .setDescription(record.get(MILESTONE.DESCRIPTION))
                        .setPlannedStartDate(record.get(MILESTONE.PLANNED_START_DATE).toInstant())
                        .setPlannedFinishDate(record.get(MILESTONE.PLANNED_FINISH_DATE).toInstant())
                        .setFactStartDate(startDateInstant)
                        .setFactFinishDate(endDateInstant)
                        .setProgressPercent(record.get(MILESTONE.PROGRESS_PERCENT));
            };

    private final DSLContext dsl;

    @Override
    public Milestone findByIdWithProject(final UUID projectId, final UUID id) {
        return dsl.selectFrom(MILESTONE)
                .where(MILESTONE.ID.eq(id))
                .fetchOptional()
                .map(mapper::map)
                .orElseThrow(() -> new DataNotFoundException("Веха к проекту не найдена!"));
    }

    @Override
    public Milestone save(final Milestone milestone) {
        final ZoneId zone = ZoneId.systemDefault();

        OffsetDateTime factStartDate = Objects.isNull(milestone.getFactStartDate())
                ? null
                : OffsetDateTime.ofInstant(milestone.getFactStartDate(), zone);
        OffsetDateTime factFinishDate = Objects.isNull(milestone.getFactFinishDate())
                ? null
                : OffsetDateTime.ofInstant(milestone.getFactFinishDate(), zone);
        return dsl.insertInto(MILESTONE)
                .set(MILESTONE.ID, UUID.randomUUID())
                .set(MILESTONE.PROJECT_ID, milestone.getProjectId())
                .set(MILESTONE.NAME, milestone.getName())
                .set(MILESTONE.DESCRIPTION, milestone.getDescription())
                .set(MILESTONE.PLANNED_START_DATE, OffsetDateTime.ofInstant(milestone.getPlannedStartDate(), zone))
                .set(MILESTONE.PLANNED_FINISH_DATE, OffsetDateTime.ofInstant(milestone.getPlannedFinishDate(), zone))
                .set(MILESTONE.FACT_START_DATE, factStartDate)
                .set(MILESTONE.FACT_FINISH_DATE, factFinishDate)
                .set(MILESTONE.PROGRESS_PERCENT, milestone.getProgressPercent())
                .returning()
                .fetchOne()
                .map(mapper);
    }

    @Override
    public void checkOnExistsWithProject(final UUID projectId, final UUID id) {
        final boolean exists = dsl.fetchExists(MILESTONE, MILESTONE.ID.eq(id).and(MILESTONE.PROJECT_ID.eq(projectId)));
        if (!exists) {
            throw new DataNotFoundException("Веха к проекту не найдена!");
        }
    }

    @Override
    public void update(final UUID id, final Milestone milestone) {
        final ZoneId zone = ZoneId.systemDefault();

        OffsetDateTime factStartDate = Objects.isNull(milestone.getFactStartDate())
                ? null
                : OffsetDateTime.ofInstant(milestone.getFactStartDate(), zone);
        OffsetDateTime factFinishDate = Objects.isNull(milestone.getFactFinishDate())
                ? null
                : OffsetDateTime.ofInstant(milestone.getFactFinishDate(), zone);
        dsl.update(MILESTONE)
                .set(MILESTONE.NAME, milestone.getName())
                .set(MILESTONE.PROJECT_ID, milestone.getProjectId())
                .set(MILESTONE.NAME, milestone.getName())
                .set(MILESTONE.DESCRIPTION, milestone.getDescription())
                .set(MILESTONE.PLANNED_START_DATE, OffsetDateTime.ofInstant(milestone.getPlannedStartDate(), zone))
                .set(MILESTONE.PLANNED_FINISH_DATE, OffsetDateTime.ofInstant(milestone.getPlannedFinishDate(), zone))
                .set(MILESTONE.FACT_START_DATE, factStartDate)
                .set(MILESTONE.FACT_FINISH_DATE, factFinishDate)
                .set(MILESTONE.PROGRESS_PERCENT, milestone.getProgressPercent())
                .where(MILESTONE.ID.eq(id))
                .execute();
    }

    @Override
    public void delete(final UUID id) {
        dsl.deleteFrom(MILESTONE)
                .where(MILESTONE.ID.eq(id))
                .execute();
    }

    @Override
    public void updateProgress(final UUID id, final short progress) {
        dsl.update(MILESTONE)
                .set(MILESTONE.PROGRESS_PERCENT, progress)
                .where(MILESTONE.ID.eq(id))
                .execute();
    }

    @Override
    public List<Milestone> findByProjectId(final UUID projectId) {
        return dsl.selectFrom(MILESTONE)
                .where(MILESTONE.PROJECT_ID.eq(projectId))
                .orderBy(MILESTONE.PROGRESS_PERCENT.desc())
                .fetch()
                .map(mapper);
    }
}
