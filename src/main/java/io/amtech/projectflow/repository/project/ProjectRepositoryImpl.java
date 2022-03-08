package io.amtech.projectflow.repository.project;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.error.DataNotFoundException;
import io.amtech.projectflow.model.project.Project;
import io.amtech.projectflow.model.project.ProjectStatus;
import io.amtech.projectflow.util.JooqFieldUtil;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static io.amtech.projectflow.jooq.tables.Project.PROJECT;
import static io.amtech.projectflow.util.SearchUtil.FROM_DATE_KEY;
import static io.amtech.projectflow.util.SearchUtil.TO_DATE_KEY;

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

    @Override
    public PagedData<Project> search(final SearchCriteria criteria) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(getConditionFromCriteria(criteria,
                                                "name",
                                                value -> PROJECT.NAME.like("%" + value + "%")));
        conditions.add(getConditionFromCriteria(criteria,
                                                "project_lead",
                                                value -> PROJECT.PROJECT_LEAD_ID.eq(UUID.fromString(value))));
        conditions.add(getConditionFromCriteria(criteria,
                                                "direction",
                                                value -> PROJECT.DIRECTION_ID.eq(UUID.fromString(value))));
        conditions.add(getConditionFromCriteria(criteria,
                                                "project_status",
                                                value -> PROJECT.STATUS.eq(ProjectStatus.of(value))));
        conditions.add(getConditionFromCriteria(criteria,
                                                "create_date_" + FROM_DATE_KEY,
                                                value -> {
                                                    OffsetDateTime createDate = OffsetDateTime.ofInstant(Instant.parse(value), ZoneId.systemDefault());
                                                    return PROJECT.CREATE_DATE.greaterOrEqual(createDate);
                                                }));
        conditions.add(getConditionFromCriteria(criteria,
                                                "create_date_" + TO_DATE_KEY,
                                                value -> {
                                                    OffsetDateTime createDate = OffsetDateTime.ofInstant(Instant.parse(value), ZoneId.systemDefault());
                                                    return PROJECT.CREATE_DATE.lessOrEqual(createDate);
                                                }));

        List<Project> projects = dsl.selectFrom(PROJECT)
                .where(conditions)
                .orderBy(JooqFieldUtil.findOrderFieldInTableOrDefault(PROJECT, criteria.getOrder(), PROJECT.ID))
                .limit(criteria.getLimit())
                .offset(criteria.getOffset())
                .fetch()
                .map(mapper);

        return new PagedData<Project>()
                .setLimit(criteria.getLimit())
                .setOffset(criteria.getOffset())
                .setData(projects);
    }

    private Condition getConditionFromCriteria(final SearchCriteria criteria,
                                               final String criteriaName,
                                               final Function<String, Condition> toCondition) {
        return criteria.getCriteriaValue(criteriaName)
                .map(toCondition)
                .orElse(DSL.trueCondition());
    }
}
