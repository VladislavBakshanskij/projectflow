package io.amtech.projectflow.controller.employee;

import io.amtech.projectflow.model.employee.Employee;
import io.amtech.projectflow.test.base.AbstractMvcTest;
import io.amtech.projectflow.test.util.TestUtil;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.jooq.SelectWhereStep;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static io.amtech.projectflow.jooq.tables.Employee.EMPLOYEE;
import static io.amtech.projectflow.repository.employee.EmployeeRepositoryImpl.mapper;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class EmployeeControllerTest extends AbstractMvcTest {
    private static final String BASE_URL = "/employees/";

    @Autowired
    private DSLContext dsl;

    static Stream<Arguments> getSuccessArgs() {
        return Stream.of(
                Arguments.arguments(UUID.fromString("1d97755d-d424-4128-8b78-ca5cb7b015c9"),
                                    readJson("getSuccess/response/positive_case_for_vova.json")),
                Arguments.arguments(UUID.fromString("34ffb834-e0ac-4fab-9900-1993f3b8ad5b"),
                                    readJson("getSuccess/response/positive_case_for_vlad.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("getSuccessArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/employee/data.sql")
    void getSuccess(final UUID id, final String response) {
        mvc.perform(TestUtil.get(BASE_URL + id)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }

    @Test
    @SneakyThrows
    void getFailed() {
        final UUID id = UUID.randomUUID();
        final String response = readJson("getFailed/response/not_found.json", id);
        mvc.perform(TestUtil.get(BASE_URL + id)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response, true));
    }

    static Stream<Arguments> createSuccessArgs() {
        return Stream.of(
                Arguments.arguments(readJson("createSuccess/request/positive_case_vlad.json"),
                                    readJson("createSuccess/response/positive_case_vlad.json")),
                Arguments.arguments(readJson("createSuccess/request/positive_case_vova.json"),
                                    readJson("createSuccess/response/positive_case_vova.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("createSuccessArgs")
    @SneakyThrows
    void createSuccess(final String request, final String response) {
        mvc.perform(TestUtil.post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response, false));

        Assertions.assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(EMPLOYEE)))
                .isOne();
    }

    static Stream<Arguments> createFailedArgs() {
        return Stream.of(
                Arguments.arguments(readJson("createFailed/request/negative_case_name_is_empty.json"),
                                    readJson("createFailed/response/negative_case_name_is_empty.json")),
                Arguments.arguments(readJson("createFailed/request/negative_case_email_is_invalid.json"),
                                    readJson("createFailed/response/negative_case_email_is_invalid.json")),
                Arguments.arguments(readJson("createFailed/request/negative_case_email_is_invalid_and_user_position_is_null.json"),
                                    readJson("createFailed/response/negative_case_email_is_invalid_and_user_position_is_null.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("createFailedArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/employee/data.sql")
    void createFailed(final String request, final String response) {
        mvc.perform(TestUtil.post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response, true));

        Assertions.assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(EMPLOYEE)))
                .isEqualTo(2);
    }

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/employee/data.sql")
    void deleteSuccess() {
        final UUID id = UUID.fromString("1d97755d-d424-4128-8b78-ca5cb7b015c9");
        mvc.perform(TestUtil.delete(BASE_URL + id)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Assertions.assertThat(transactionalUtil.txRun(() -> dsl.fetchExists(EMPLOYEE, EMPLOYEE.ID.eq(id))))
                .isFalse();

        Assertions.assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(EMPLOYEE)))
                .isOne();
    }

    static Stream<Arguments> updateSuccessArgs() {
        return Stream.of(
                Arguments.arguments(UUID.fromString("1d97755d-d424-4128-8b78-ca5cb7b015c9"),
                                    readJson("updateSuccess/request/positive_case_email_and_position.json")),
                Arguments.arguments(UUID.fromString("1d97755d-d424-4128-8b78-ca5cb7b015c9"),
                                    readJson("updateSuccess/request/positive_case_all.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("updateSuccessArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/employee/data.sql")
    void updateSuccess(final UUID id, final String request) {
        final SelectConditionStep<Record> selectEmployeeNotEqualUpdatedEmployee = dsl.selectFrom(EMPLOYEE).where(EMPLOYEE.ID.notEqual(id));
        final Set<Employee> employeesBeforeUpdate = transactionalUtil.txRun(() -> selectEmployeeNotEqualUpdatedEmployee.fetchSet(mapper));

        mvc.perform(TestUtil.put(BASE_URL + id)
                            .content(request)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        final Set<Employee> employeesAfterUpdate = transactionalUtil.txRun(() -> selectEmployeeNotEqualUpdatedEmployee.fetchSet(mapper));

        Assertions.assertThat(employeesBeforeUpdate)
                .isNotEmpty()
                .contains(employeesAfterUpdate.toArray(Employee[]::new));
    }

    static Stream<Arguments> updateFailedArgs() {
        return Stream.of(
                Arguments.arguments(UUID.fromString("1d97755d-d424-4128-8b78-ca5cb7b015c9"),
                                    readJson("updateFailed/request/negative_case_name_is_empty.json"),
                                    readJson("updateFailed/response/negative_case_name_is_empty.json")),
                Arguments.arguments(UUID.fromString("1d97755d-d424-4128-8b78-ca5cb7b015c9"),
                                    readJson("updateFailed/request/negative_case_email_is_invalid.json"),
                                    readJson("updateFailed/response/negative_case_email_is_invalid.json")),
                Arguments.arguments(UUID.fromString("1d97755d-d424-4128-8b78-ca5cb7b015c9"),
                                    readJson("updateFailed/request/negative_case_email_is_invalid_and_position_is_null.json"),
                                    readJson("updateFailed/response/negative_case_email_is_invalid_and_position_is_null.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("updateFailedArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/employee/data.sql")
    void updateFailed(final UUID id, final String request, final String response) {
        SelectWhereStep<Record> selectEmployee = dsl.selectFrom(EMPLOYEE);
        final Set<Employee> employeesBeforeUpdate = transactionalUtil.txRun(() -> selectEmployee.fetchSet(mapper));

        mvc.perform(TestUtil.put(BASE_URL + id)
                            .content(request)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(response, true));

        final Set<Employee> employeesAfterUpdate = transactionalUtil.txRun(() -> selectEmployee.fetchSet(mapper));

        Assertions.assertThat(employeesBeforeUpdate)
                .isNotEmpty()
                .contains(employeesAfterUpdate.toArray(Employee[]::new));
    }

    private static String readJson(final String path, final Object... args) {
        final String content = TestUtil.readContentFromClassPathResource("json/EmployeeControllerTest/" + path);
        return String.format(content, args);
    }
}
