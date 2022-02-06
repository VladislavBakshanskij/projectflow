package io.amtech.projectflow.repository.direction;

import io.amtech.projectflow.error.DataNotFoundException;
import io.amtech.projectflow.model.Direction;
import io.amtech.projectflow.model.DirectionWithLeadName;
import io.amtech.projectflow.repository.employee.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static io.amtech.projectflow.jooq.tables.Direction.DIRECTION;
import static io.amtech.projectflow.jooq.tables.Employee.EMPLOYEE;

@Repository
@RequiredArgsConstructor
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
