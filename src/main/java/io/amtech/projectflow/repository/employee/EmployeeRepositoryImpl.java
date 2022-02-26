package io.amtech.projectflow.repository.employee;

import io.amtech.projectflow.app.PagedData;
import io.amtech.projectflow.app.SearchCriteria;
import io.amtech.projectflow.error.DataNotFoundException;
import io.amtech.projectflow.model.employee.Employee;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

import static io.amtech.projectflow.jooq.tables.Employee.EMPLOYEE;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {
    public static final RecordMapper<Record, Employee> mapper = record -> new Employee()
            .setId(record.get(EMPLOYEE.ID))
            .setEmail(record.get(EMPLOYEE.EMAIL))
            .setFired(record.get(EMPLOYEE.IS_FIRED))
            .setName(record.get(EMPLOYEE.NAME))
            .setPhone(record.get(EMPLOYEE.PHONE))
            .setUserPosition(record.get(EMPLOYEE.POSITION));

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
                .set(EMPLOYEE.POSITION, e.getUserPosition())
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
                                                value -> EMPLOYEE.NAME.like("%" + value + "%")));
        conditions.add(getConditionFromCriteria(searchCriteria, "isFired",
                                                value -> EMPLOYEE.IS_FIRED.eq(Boolean.valueOf(value))));
        conditions.add(getConditionFromCriteria(searchCriteria, "phone",
                                                value -> EMPLOYEE.PHONE.like("%" + value + "%")));
        conditions.add(getConditionFromCriteria(searchCriteria, "email",
                                                value -> EMPLOYEE.EMAIL.like("%" + value + "%")));

        List<Employee> employees = dsl.selectFrom(EMPLOYEE)
                .where(conditions)
                .limit(searchCriteria.getLimit())
                .offset(searchCriteria.getOffset())
                .fetch()
                .map(mapper);

        return new PagedData<Employee>()
                .setOffset(searchCriteria.getOffset())
                .setLimit(searchCriteria.getLimit())
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
