package io.amtech.projectflow.repository.project;

import io.amtech.projectflow.app.Meta;
import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.error.DataNotFoundException;
import io.amtech.projectflow.jooq.tables.Employee;
import io.amtech.projectflow.model.employee.UserPosition;
import io.amtech.projectflow.model.project.*;
import io.amtech.projectflow.util.JooqFieldUtil;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static io.amtech.projectflow.jooq.tables.Direction.DIRECTION;
import static io.amtech.projectflow.jooq.tables.Employee.EMPLOYEE;
import static io.amtech.projectflow.jooq.tables.Project.PROJECT;
import static io.amtech.projectflow.util.SearchUtil.FROM_DATE_KEY;
import static io.amtech.projectflow.util.SearchUtil.TO_DATE_KEY;
import static java.util.Objects.requireNonNull;

@Repository
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class ProjectRepositoryImpl implements ProjectRepository {
    public static final RecordMapper<Record, Project> mapper = record -> new Project()
            .setId(record.get(PROJECT.ID))
            .setName(record.get(PROJECT.NAME))
            .setDescription(record.get(PROJECT.DESCRIPTION))
            .setStatus(ProjectStatus.from(record.get(PROJECT.STATUS)))
            .setProjectLeadId(record.get(PROJECT.PROJECT_LEAD_ID))
            .setDirectionId(record.get(PROJECT.DIRECTION_ID))
            .setCreateDate(Instant.from(record.get(PROJECT.CREATE_DATE)));

    public static final RecordMapper<Record, ProjectWithEmployeeDirection> mapperWithEmployeeDirection = record -> new ProjectWithEmployeeDirection()
            .setId(record.get(PROJECT.ID))
            .setName(record.get(PROJECT.NAME))
            .setDescription(record.get(PROJECT.DESCRIPTION))
            .setStatus(ProjectStatus.from(record.get(PROJECT.STATUS)))
            .setCreateDate(Instant.from(record.get(PROJECT.CREATE_DATE)))
            .setLead(new Lead()
                             .setId(record.get(EMPLOYEE.ID))
                             .setName(record.get(EMPLOYEE.NAME)))
            .setDirection(new ProjectDirection()
                                  .setId(record.get(DIRECTION.ID))
                                  .setName(record.get(DIRECTION.NAME)));

    private static final Employee DIRECTION_LEAD_ALIAS = EMPLOYEE.as("direction_lead");

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
    public ProjectWithEmployeeDirection findById(final UUID id) {
        return getProjectSelect()
                .where(PROJECT.ID.eq(id))
                .fetchOptional()
                .map(mapperWithEmployeeDirection::map)
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
    public void updateStatus(final UUID id, final ProjectStatus status) {
        dsl.update(PROJECT)
                .set(PROJECT.STATUS, requireNonNull(status).toJooqStatus())
                .where(PROJECT.ID.eq(id))
                .execute();
    }

    @Override
    public Project save(final Project project) {
        return dsl.insertInto(PROJECT)
                .set(PROJECT.ID, UUID.randomUUID())
                .set(PROJECT.NAME, project.getName())
                .set(PROJECT.STATUS, ProjectStatus.UNAPPROVED.toJooqStatus())
                .set(PROJECT.CREATE_DATE, OffsetDateTime.now())
                .set(PROJECT.PROJECT_LEAD_ID, project.getProjectLeadId())
                .set(PROJECT.DIRECTION_ID, project.getDirectionId())
                .set(PROJECT.DESCRIPTION, project.getDescription())
                .returning()
                .fetchOne()
                .map(mapper);
    }

    @Override
    public PagedData<ProjectWithEmployeeDirection> search(final SearchCriteria criteria) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(getConditionFromCriteria(criteria,
                                                "name",
                                                value -> DSL.lower(PROJECT.NAME).like(("%" + value + "%").toLowerCase())));
        conditions.add(getConditionFromCriteria(criteria,
                                                "project_lead",
                                                value -> PROJECT.PROJECT_LEAD_ID.eq(UUID.fromString(value))));
        conditions.add(getConditionFromCriteria(criteria,
                                                "direction",
                                                value -> PROJECT.DIRECTION_ID.eq(UUID.fromString(value))));
        conditions.add(getConditionFromCriteria(criteria,
                                                "project_status",
                                                value -> PROJECT.STATUS.eq(ProjectStatus.from(value).toJooqStatus())));
        conditions.add(getConditionFromCriteria(criteria,
                                                "create_date_" + FROM_DATE_KEY,
                                                value -> {
                                                    final OffsetDateTime createDate = OffsetDateTime.ofInstant(Instant.parse(value), ZoneId.systemDefault());
                                                    return PROJECT.CREATE_DATE.greaterOrEqual(createDate);
                                                }));
        conditions.add(getConditionFromCriteria(criteria,
                                                "create_date_" + TO_DATE_KEY,
                                                value -> {
                                                    final OffsetDateTime createDate = OffsetDateTime.ofInstant(Instant.parse(value), ZoneId.systemDefault());
                                                    return PROJECT.CREATE_DATE.lessOrEqual(createDate);
                                                }));

        final List<ProjectWithEmployeeDirection> projects = getProjectSelect()
                .where(conditions)
                .orderBy(JooqFieldUtil.findOrderFieldInTableOrDefault(PROJECT, criteria.getOrder(), PROJECT.ID))
                .limit(criteria.getLimit())
                .offset(criteria.getOffset())
                .fetch()
                .map(mapperWithEmployeeDirection);

        final long totalPages = dsl.fetchCount(PROJECT, conditions) / criteria.getLimit();

        return new PagedData<ProjectWithEmployeeDirection>()
                .setMeta(new Meta()
                                 .setLimit(criteria.getLimit())
                                 .setOffset(criteria.getOffset())
                                 .setTotalPages(totalPages))
                .setData(projects);
    }

    @Override
    public PagedData<ProjectWithEmployeeDirection> searchForDirector(final SearchCriteria searchCriteria) {
        List<ProjectWithEmployeeDirection> projects = getProjectSelect()
                .orderBy(PROJECT.CREATE_DATE.desc())
                .limit(searchCriteria.getLimit())
                .offset(searchCriteria.getOffset())
                .fetch()
                .map(mapperWithEmployeeDirection);
        final int totalPages = dsl.fetchCount(PROJECT) / searchCriteria.getLimit();
        return new PagedData<ProjectWithEmployeeDirection>()
                .setMeta(new Meta()
                                 .setLimit(searchCriteria.getLimit())
                                 .setOffset(searchCriteria.getOffset())
                                 .setTotalPages(totalPages))
                .setData(projects);
    }

    @Override
    public PagedData<ProjectWithEmployeeDirection> searchForPosition(final SearchCriteria searchCriteria) {
        final UUID userId = searchCriteria.getCriteriaValue("user")
                .map(UUID::fromString)
                .orElseThrow(() -> new DataNotFoundException("Пользователь не задан"));
        final UserPosition userPosition = searchCriteria.getCriteriaValue("position")
                .map(UserPosition::from)
                .orElseThrow(() -> new DataNotFoundException("Роль не задана"));

        List<Condition> conditions;
        if (userPosition == UserPosition.DIRECTION_LEAD) {
            conditions = List.of(DIRECTION_LEAD_ALIAS.ID.eq(userId),
                                 DIRECTION_LEAD_ALIAS.POSITION.eq(userPosition.toJooqPosition()));
        } else {
            conditions = List.of(EMPLOYEE.ID.eq(userId),
                                 EMPLOYEE.POSITION.eq(userPosition.toJooqPosition()));
        }

        final SelectConditionStep<Record9<UUID, String, String, OffsetDateTime, io.amtech.projectflow.jooq.enums.ProjectStatus, UUID, String, UUID, String>>
                projectSelect = getProjectSelect().where(conditions);

        final List<ProjectWithEmployeeDirection> projects = projectSelect
                .orderBy(PROJECT.CREATE_DATE.desc())
                .limit(searchCriteria.getLimit())
                .offset(searchCriteria.getOffset())
                .fetch()
                .map(mapperWithEmployeeDirection);

        final int totalPages = dsl.fetchCount(projectSelect) / searchCriteria.getLimit();
        return new PagedData<ProjectWithEmployeeDirection>()
                .setMeta(new Meta()
                                 .setLimit(searchCriteria.getLimit())
                                 .setOffset(searchCriteria.getOffset())
                                 .setTotalPages(totalPages))
                .setData(projects);
    }

    private SelectOnConditionStep<Record9<UUID, String, String, OffsetDateTime,
            io.amtech.projectflow.jooq.enums.ProjectStatus, UUID, String, UUID, String>> getProjectSelect() {
        return dsl.select(PROJECT.ID, PROJECT.NAME, PROJECT.DESCRIPTION, PROJECT.CREATE_DATE,
                          PROJECT.STATUS, EMPLOYEE.ID, EMPLOYEE.NAME, DIRECTION.ID, DIRECTION.NAME)
                .from(PROJECT)
                .leftJoin(EMPLOYEE).on(EMPLOYEE.ID.eq(PROJECT.PROJECT_LEAD_ID))
                .leftJoin(DIRECTION).on(DIRECTION.ID.eq(PROJECT.DIRECTION_ID))
                .leftJoin(EMPLOYEE.as(DIRECTION_LEAD_ALIAS)).on(DIRECTION_LEAD_ALIAS.ID.eq(DIRECTION.LEAD_ID));
    }

    private Condition getConditionFromCriteria(final SearchCriteria criteria,
                                               final String criteriaName,
                                               final Function<String, Condition> toCondition) {
        return criteria.getCriteriaValue(criteriaName)
                .map(toCondition)
                .orElse(DSL.trueCondition());
    }
}
