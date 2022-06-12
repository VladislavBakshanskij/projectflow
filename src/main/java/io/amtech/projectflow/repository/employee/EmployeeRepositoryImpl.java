package io.amtech.projectflow.repository.employee;

import io.amtech.projectflow.app.Meta;
import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.error.DataNotFoundException;
import io.amtech.projectflow.model.employee.Employee;
import io.amtech.projectflow.model.employee.UserPosition;
import io.amtech.projectflow.util.JooqFieldUtil;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static io.amtech.projectflow.jooq.tables.Employee.EMPLOYEE;

@Repository
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class EmployeeRepositoryImpl implements EmployeeRepository {
    public static final RecordMapper<Record, Employee> mapper = record -> new Employee()
            .setId(record.get(EMPLOYEE.ID))
            .setEmail(record.get(EMPLOYEE.EMAIL))
            .setFired(record.get(EMPLOYEE.IS_FIRED))
            .setName(record.get(EMPLOYEE.NAME))
            .setPhone(record.get(EMPLOYEE.PHONE))
            .setUserPosition(UserPosition.from(record.get(EMPLOYEE.POSITION)));

    private final DSLContext dsl;

    @Override
    public Employee findById(UUID id) {
        return dsl.selectFrom(EMPLOYEE)
                .where(EMPLOYEE.ID.eq(id))
                .fetchOptional()
                .map(mapper::map)
                .orElseThrow(() -> new DataNotFoundException("Сотрудник " + id + " не найден"));
    }

    @Override
    public Employee save(Employee e) {
        return dsl.insertInto(EMPLOYEE)
                .set(EMPLOYEE.ID, UUID.randomUUID())
                .set(EMPLOYEE.EMAIL, e.getEmail())
                .set(EMPLOYEE.IS_FIRED, e.isFired())
                .set(EMPLOYEE.NAME, e.getName())
                .set(EMPLOYEE.PHONE, e.getPhone())
                .set(EMPLOYEE.POSITION, e.getUserPosition().toJooqPosition())
                .returning()
                .fetchOne()
                .map(mapper);
    }

    @Override
    public void update(UUID id, Employee e) {
        dsl.update(EMPLOYEE)
                .set(EMPLOYEE.EMAIL, e.getEmail())
                .set(EMPLOYEE.IS_FIRED, e.isFired())
                .set(EMPLOYEE.NAME, e.getName())
                .set(EMPLOYEE.PHONE, e.getPhone())
                .where(EMPLOYEE.ID.eq(id))
                .execute();
    }

    @Override
    public void delete(UUID id) {
        dsl.delete(EMPLOYEE)
                .where(EMPLOYEE.ID.eq(id))
                .execute();
    }

    @Override
    public PagedData<Employee> search(SearchCriteria searchCriteria) {
        List<Condition> conditions = new ArrayList<>();

        conditions.add(getConditionFromCriteria(searchCriteria, "name",
                                                value -> DSL.lower(EMPLOYEE.NAME).like(("%" + value + "%").toLowerCase())));
        conditions.add(getConditionFromCriteria(searchCriteria, "fired",
                                                value -> EMPLOYEE.IS_FIRED.eq(Boolean.valueOf(value))));
        conditions.add(getConditionFromCriteria(searchCriteria, "phone",
                                                value -> EMPLOYEE.PHONE.like("%" + value + "%")));
        conditions.add(getConditionFromCriteria(searchCriteria, "email",
                                                value -> DSL.lower(EMPLOYEE.EMAIL).like(("%" + value + "%").toLowerCase())));
        conditions.add(getConditionFromCriteria(searchCriteria, "position",
                                                value -> EMPLOYEE.POSITION.eq(UserPosition.from(value).toJooqPosition())));

        List<Employee> employees = dsl.selectFrom(EMPLOYEE)
                .where(conditions)
                .orderBy(JooqFieldUtil.findOrderFieldInTableOrDefault(EMPLOYEE, searchCriteria.getOrder(), EMPLOYEE.ID))
                .limit(searchCriteria.getLimit())
                .offset(searchCriteria.getOffset())
                .fetch()
                .map(mapper);

        final long totalPages = dsl.fetchCount(EMPLOYEE, conditions) / searchCriteria.getLimit();

        return new PagedData<Employee>()
                .setMeta(new Meta()
                                 .setOffset(searchCriteria.getOffset())
                                 .setLimit(searchCriteria.getLimit())
                                 .setTotalPages(totalPages))
                .setData(employees);
    }

    @Override
    public void checkOnExists(UUID id) {
        if (!dsl.fetchExists(EMPLOYEE, EMPLOYEE.ID.eq(id))) {
            throw new DataNotFoundException("Сотрудник не найден!");
        }
    }

    private Condition getConditionFromCriteria(final SearchCriteria criteria,
                                               final String criteriaName,
                                               final Function<String, Condition> toCondition) {
        return criteria.getCriteriaValue(criteriaName)
                .map(toCondition)
                .orElse(DSL.trueCondition());
    }
}
