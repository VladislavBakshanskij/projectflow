package io.amtech.projectflow.repository.direction;

import io.amtech.projectflow.model.Direction;
import io.amtech.projectflow.test.base.AbstractIntegrationTest;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static io.amtech.projectflow.jooq.tables.Direction.DIRECTION;

class DirectionRepositoryTest extends AbstractIntegrationTest {
    private static final UUID DIRECTION_LEAD_ID = UUID.fromString("1d97755d-d424-4128-8b78-ca5cb7b015c9");
    private static final String DIRECTION_LEAD_NAME = "Vova";
    public static final String DIRECTION_FAKE_NAME = "test name";

    @Autowired
    private DirectionRepository directionRepository;

    @Autowired
    private DSLContext dsl;

    @Test
    @Sql(scripts = "classpath:db/direction/data.sql")
    @SneakyThrows
    void saveSuccess() {
        Direction direction = new Direction()
                .setName(DIRECTION_FAKE_NAME)
                .setLeadId(DIRECTION_LEAD_ID);
        Assertions.assertThat(transactionalUtil.txRun(() -> directionRepository.save(direction)))
                .isNotNull()
                .satisfies(d -> {
                    Assertions.assertThat(d.getId())
                            .isNotNull();
                    Assertions.assertThat(d.getName())
                            .isNotBlank()
                            .isEqualTo(DIRECTION_FAKE_NAME);
                    Assertions.assertThat(d.getLeadId())
                            .isNotNull()
                            .isEqualTo(DIRECTION_LEAD_ID);
                    Assertions.assertThat(d.getLeadName())
                            .isNotBlank()
                            .isEqualTo(DIRECTION_LEAD_NAME);
                });

        Assertions.assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(DIRECTION)))
                .isEqualTo(2);
    }

    @Test
    @Sql(scripts = "classpath:db/direction/data.sql")
    @SneakyThrows
    void findByIdSuccess() {
        final UUID id = UUID.fromString("02f27e01-2ac4-4518-9c66-d0b7e2259a6f");
        Assertions.assertThat(transactionalUtil.txRun(() -> directionRepository.findById(id)))
                .isNotNull()
                .satisfies(d -> {
                    Assertions.assertThat(d.getId())
                            .isNotNull();
                    Assertions.assertThat(d.getName())
                            .isNotBlank()
                            .isEqualTo("super direction");
                    Assertions.assertThat(d.getLeadId())
                            .isNotNull()
                            .isEqualTo(DIRECTION_LEAD_ID);
                    Assertions.assertThat(d.getLeadName())
                            .isNotBlank()
                            .isEqualTo(DIRECTION_LEAD_NAME);
                });
    }
}