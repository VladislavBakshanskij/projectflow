package io.amtech.projectflow.repository.employee;

import io.amtech.projectflow.model.Employee;
import io.amtech.projectflow.model.UserPosition;
import io.amtech.projectflow.test.base.AbstractIntegrationTest;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static io.amtech.projectflow.jooq.Tables.EMPLOYEE;

class EmployeeRepositoryTest extends AbstractIntegrationTest {
    private static final String FAKE_NAME = "Vova";
    private static final String FAKE_EMAIL = "1_1_super@email.com";
    private static final String FAKE_PHONE = "2-2-2-2-2";
    private static final UserPosition FAKE_POSITION = UserPosition.PROJECT_LEAD;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private DSLContext dsl;

    @Test
    @SneakyThrows
    void saveSuccess() {
        Employee e = new Employee()
                .setName(FAKE_NAME)
                .setEmail(FAKE_EMAIL)
                .setPhone(FAKE_PHONE)
                .setFired(false)
                .setUserPosition(FAKE_POSITION);
        Assertions.assertThat(transactionalUtil.txRun(() -> employeeRepository.save(e)))
                .isNotNull()
                .satisfies(em -> {
                    Assertions.assertThat(em.getEmail())
                            .isNotBlank()
                            .isEqualTo(FAKE_EMAIL);
                    Assertions.assertThat(em.getName())
                            .isNotBlank()
                            .isEqualTo(FAKE_NAME);
                    Assertions.assertThat(em.getPhone())
                            .isNotBlank()
                            .isEqualTo(FAKE_PHONE);
                    Assertions.assertThat(em.getUserPosition())
                            .isNotNull()
                            .isEqualTo(FAKE_POSITION);
                    Assertions.assertThat(em.isFired())
                            .isFalse();
                });

        Assertions.assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(EMPLOYEE)))
                .isOne();
    }

    @Test
    @Sql(scripts = "classpath:db/employee/data.sql")
    @SneakyThrows
    void findByIdSuccess() {
        Assertions.assertThat(transactionalUtil.txRun(() -> employeeRepository.findById(UUID.fromString("1d97755d-d424-4128-8b78-ca5cb7b015c9"))))
                .isNotNull()
                .satisfies(em -> {
                    Assertions.assertThat(em.getEmail())
                            .isNotBlank()
                            .isEqualTo(FAKE_EMAIL);
                    Assertions.assertThat(em.getName())
                            .isNotBlank()
                            .isEqualTo(FAKE_NAME);
                    Assertions.assertThat(em.getPhone())
                            .isNotBlank()
                            .isEqualTo(FAKE_PHONE);
                    Assertions.assertThat(em.getUserPosition())
                            .isNotNull()
                            .isEqualTo(FAKE_POSITION);
                    Assertions.assertThat(em.isFired())
                            .isFalse();
                });
        ;
    }
}