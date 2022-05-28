package io.amtech.projectflow.repository.direction;

import io.amtech.projectflow.app.Meta;
import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.error.DataNotFoundException;
import io.amtech.projectflow.model.direction.Direction;
import io.amtech.projectflow.model.direction.DirectionWithLeadName;
import io.amtech.projectflow.util.JooqFieldUtil;
import lombok.RequiredArgsConstructor;
import org.jooq.*;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static io.amtech.projectflow.jooq.tables.Direction.DIRECTION;
import static io.amtech.projectflow.jooq.tables.Employee.EMPLOYEE;

@Repository
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class DirectionRepositoryImpl implements DirectionRepository {
    private static final Field<String> DIRECTION_LEAD_NAME_FIELD = DSL.field(DSL.name("leadName"), String.class);

    private static final RecordMapper<Record, DirectionWithLeadName> mapper =
            record -> new DirectionWithLeadName()
                    .setId(record.get(DIRECTION.ID))
                    .setName(record.get(DIRECTION.NAME))
                    .setLeadId(record.get(DIRECTION.LEAD_ID))
                    .setLeadName(record.get(DIRECTION_LEAD_NAME_FIELD));

    private final DSLContext dsl;

    @Override
    public DirectionWithLeadName save(final Direction direction) {
        final UUID id = UUID.randomUUID();

        dsl.insertInto(DIRECTION)
                .set(DIRECTION.ID, id)
                .set(DIRECTION.NAME, direction.getName())
                .set(DIRECTION.LEAD_ID, direction.getLeadId())
                .execute();
        return findById(id);
    }

    @Override
    public DirectionWithLeadName findById(final UUID id) {
        return dsl.select(DIRECTION.ID, DIRECTION.LEAD_ID, DIRECTION.NAME,
                        EMPLOYEE.NAME.as(DIRECTION_LEAD_NAME_FIELD))
                .from(DIRECTION)
                    .leftJoin(EMPLOYEE).on(EMPLOYEE.ID.eq(DIRECTION.LEAD_ID))
                .where(DIRECTION.ID.eq(id))
                .fetchOptional()
                .map(mapper::map)
                .orElseThrow(() -> new DataNotFoundException("Направление не найдено"));
    }

    @Override
    public PagedData<DirectionWithLeadName> search(final SearchCriteria searchCriteria) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(getConditionFromCriteria(searchCriteria,
                                                "name",
                                                value -> DSL.lower(DIRECTION.NAME).like(("%" + value + "%").toLowerCase())));
        conditions.add(getConditionFromCriteria(searchCriteria,
                                                "leadId",
                                                value -> DIRECTION.LEAD_ID.eq(UUID.fromString(value))));
        conditions.add(getConditionFromCriteria(searchCriteria,
                                                "leadName",
                                                value -> DSL.lower(DIRECTION_LEAD_NAME_FIELD).like(("%" + value + "%").toLowerCase())));

        final SelectConditionStep<Record4<UUID, UUID, String, String>> directionSelectQuery =
                dsl.select(DIRECTION.ID, DIRECTION.LEAD_ID, DIRECTION.NAME,
                           EMPLOYEE.NAME.as(DIRECTION_LEAD_NAME_FIELD))
                        .from(DIRECTION)
                        .leftJoin(EMPLOYEE).on(EMPLOYEE.ID.eq(DIRECTION.LEAD_ID))
                        .where(conditions);
        List<DirectionWithLeadName> directions = directionSelectQuery
                .orderBy(JooqFieldUtil.findOrderFieldInTableOrDefault(DIRECTION, searchCriteria.getOrder(), DIRECTION.NAME))
                .limit(searchCriteria.getLimit())
                .offset(searchCriteria.getOffset())
                .fetch()
                .map(mapper);

        final long totalPages = dsl.fetchCount(directionSelectQuery) / searchCriteria.getLimit();

        return new PagedData<DirectionWithLeadName>()
                .setMeta(new Meta()
                                 .setLimit(searchCriteria.getLimit())
                                 .setOffset(searchCriteria.getOffset())
                                 .setTotalPages(totalPages))
                .setData(directions);
    }


    private Condition getConditionFromCriteria(final SearchCriteria criteria,
                                               final String criteriaName,
                                               final Function<String, Condition> toCondition) {
        return criteria.getCriteriaValue(criteriaName)
                .map(toCondition)
                .orElse(DSL.trueCondition());
    }

    @Override
    public void update(final UUID id, final Direction direction) {
        dsl.update(DIRECTION)
                .set(DIRECTION.NAME, direction.getName())
                .set(DIRECTION.LEAD_ID, direction.getLeadId())
                .where(DIRECTION.ID.eq(id))
                .execute();
    }

    @Override
    public void delete(final UUID id) {
        dsl.deleteFrom(DIRECTION)
                .where(DIRECTION.ID.eq(id))
                .execute();
    }

    @Override
    public void checkOnExists(final UUID id) {
        if (!dsl.fetchExists(DIRECTION, DIRECTION.ID.eq(id))) {
            throw new DataNotFoundException("Направление не найдено");
        }
    }
}
