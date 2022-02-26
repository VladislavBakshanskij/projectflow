package io.amtech.projectflow.repository.auth.user;

import io.amtech.projectflow.error.DataNotFoundException;
import io.amtech.projectflow.model.auth.User;
import io.amtech.projectflow.model.auth.UserWithEmployee;
import io.amtech.projectflow.repository.employee.EmployeeRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Repository;

import java.util.UUID;

import static io.amtech.projectflow.jooq.tables.AuthUser.AUTH_USER;
import static io.amtech.projectflow.jooq.tables.Employee.EMPLOYEE;

@Repository
@RequiredArgsConstructor
public class AuthUserRepositoryImpl implements AuthUserRepository {
    private static final RecordMapper<Record, User> mapper = record -> new User()
            .setEmployeeId(record.get(AUTH_USER.EMPLOYEE_ID))
            .setLogin(record.get(AUTH_USER.LOGIN))
            .setLocked(record.get(AUTH_USER.IS_LOCKED))
            .setPassword(record.get(AUTH_USER.PASSWORD));

    private final DSLContext dsl;

    @Override
    public User findByEmployeeId(final UUID id) {
        return dsl.selectFrom(AUTH_USER)
                .where(AUTH_USER.EMPLOYEE_ID.eq(id))
                .fetchOptional()
                .map(mapper::map)
                .orElseThrow(() -> new DataNotFoundException("Сотрудник " + id + " не найден"));
    }

    @Override
    public UserWithEmployee findWithEmployeeByEmployeeId(final UUID id) {
        return dsl.select(AUTH_USER.EMPLOYEE_ID, AUTH_USER.LOGIN, AUTH_USER.PASSWORD, AUTH_USER.IS_LOCKED,
                        EMPLOYEE.ID, EMPLOYEE.EMAIL, EMPLOYEE.IS_FIRED, EMPLOYEE.NAME, EMPLOYEE.PHONE, EMPLOYEE.POSITION)
                .from(AUTH_USER)
                    .leftJoin(EMPLOYEE).on(EMPLOYEE.ID.eq(AUTH_USER.EMPLOYEE_ID))
                .where(AUTH_USER.EMPLOYEE_ID.eq(id))
                .fetchOptional()
                .map(record -> new UserWithEmployee()
                        .setUser(mapper.map(record))
                        .setEmployee(EmployeeRepositoryImpl.mapper.map(record)))
                .orElseThrow(() -> new DataNotFoundException("Сотрудник " + id + " не найден"));
    }

    @Override
    public UserWithEmployee findByLogin(final String username) {
        return dsl.select(AUTH_USER.EMPLOYEE_ID, AUTH_USER.LOGIN, AUTH_USER.PASSWORD, AUTH_USER.IS_LOCKED)
                .from(AUTH_USER)
                    .leftJoin(EMPLOYEE).on(EMPLOYEE.ID.eq(AUTH_USER.EMPLOYEE_ID))
                .where(AUTH_USER.LOGIN.eq(username))
                .fetchOptional()
                .map(record -> new UserWithEmployee())
                .orElseThrow(() -> new DataNotFoundException("Сотрудник " + username + " не найден"));
    }

    @Override
    public User findByUsername(String username) {
        return dsl.selectFrom(AUTH_USER)
                .where(AUTH_USER.LOGIN.eq(username))
                .fetchOptional()
                .map(mapper::map)
                .orElseThrow(() -> new DataNotFoundException("Сотрудник " + username + " не найден"));
    }
}
