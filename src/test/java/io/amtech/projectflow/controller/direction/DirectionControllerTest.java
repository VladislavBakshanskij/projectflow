package io.amtech.projectflow.controller.direction;

import io.amtech.projectflow.model.direction.Direction;
import io.amtech.projectflow.model.direction.DirectionWithLeadName;
import io.amtech.projectflow.repository.direction.DirectionRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.amtech.projectflow.jooq.tables.Direction.DIRECTION;
import static io.amtech.projectflow.test.util.TestUtil.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DirectionControllerTest extends AbstractMvcTest {
    private static final String BASE_URL = "/directions/";

    @Autowired
    private DSLContext dsl;

    @Autowired
    private DirectionRepository directionRepository;

    static Stream<Arguments> createSuccessArgs() {
        return Stream.of(
                Arguments.arguments(readJson("createSuccess/request/positive_case.json"),
                                    readJson("createSuccess/response/positive_case.json"))
        );
    }

    static Stream<Arguments> createFailedArgs() {
        return Stream.of(
                Arguments.arguments(readJson("createFailed/request/negative_case_name_is_empty.json"),
                                    readJson("createFailed/response/negative_case_name_is_empty.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments(readJson("createFailed/request/negative_case_lead_not_found.json"),
                                    readJson("createFailed/response/negative_case_lead_not_found.json"),
                                    HttpStatus.NOT_FOUND)
        );
    }

    static Stream<Arguments> updateSuccessArgs() {
        return Stream.of(
                Arguments.arguments(UUID.fromString("02f27e01-2ac4-4518-9c66-d0b7e2259a6f"),
                                    readJson("updateSuccess/request/positive_case.json"),
                                    new DirectionWithLeadName()
                                            .setName("SOME DIRECTION")
                                            .setLeadId(UUID.fromString("34ffb834-e0ac-4fab-9900-1993f3b8ad5b"))
                                            .setLeadName("Vlad")),
                Arguments.arguments(UUID.fromString("4feef2c8-ca3e-4bdf-baf6-39032f450134"),
                                    readJson("updateSuccess/request/positive_case_one_field.json"),
                                    new DirectionWithLeadName()
                                            .setName("new direction name")
                                            .setLeadId(UUID.fromString("1d97755d-d424-4128-8b78-ca5cb7b015c9"))
                                            .setLeadName("Vova"))
        );
    }

    static Stream<Arguments> updateFailedArgs() {
        return Stream.of(
                Arguments.arguments(UUID.fromString("02f27e01-2ac4-4518-9c66-d0b7e2259a6f"),
                                    readJson("updateFailed/request/negative_case_name_is_empty.json"),
                                    readJson("updateFailed/response/negative_case_name_is_empty.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments(UUID.fromString("02f27e01-2ac4-4518-9c66-d0b7e2259a6f"),
                                    readJson("updateFailed/request/negative_case_lead_not_found.json"),
                                    readJson("updateFailed/response/negative_case_lead_not_found.json"),
                                    HttpStatus.NOT_FOUND)
        );
    }

    private static String readJson(final String path, final Object... args) {
        final String content = TestUtil.readContentFromClassPathResource("/json/DirectionControllerTest/" + path);
        return String.format(content, args);
    }

    @ParameterizedTest
    @MethodSource("createSuccessArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/direction/data.sql")
    void createSuccess(final String request, final String response) {
        mvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response, false));
    }

    @ParameterizedTest
    @MethodSource("createFailedArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/direction/data.sql")
    void createFailed(final String request, final String response, final HttpStatus status) {
        mvc.perform(post(BASE_URL)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                .andExpect(status().is(status.value()))
                .andExpect(content().json(response));
    }

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/direction/data.sql")
    void getSuccess() {
        final UUID id = UUID.fromString("4feef2c8-ca3e-4bdf-baf6-39032f450134");
        final String response = readJson("getSuccess/response/positive_case.json", id);

        mvc.perform(get(BASE_URL + id)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response, false));
    }

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/direction/data.sql")
    void getFailed() {
        final UUID id = UUID.randomUUID();
        final String response = readJson("getFailed/response/negative_case_direction_not_found.json");

        mvc.perform(get(BASE_URL + id)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response, true));
    }

    @ParameterizedTest
    @MethodSource("updateSuccessArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/direction/data.sql")
    void updateSuccess(final UUID id, final String request, final DirectionWithLeadName d) {
        final SelectConditionStep<Record> selectDirectionsNotEqualUpdate = dsl.selectFrom(DIRECTION)
                .where(DIRECTION.ID.notEqual(id));
        final Function<Record, Direction> mapper = record -> new Direction()
                .setId(record.get(DIRECTION.ID))
                .setName(record.get(DIRECTION.NAME))
                .setLeadId(record.get(DIRECTION.LEAD_ID));

        final List<Direction> directionsBeforeUpdate = transactionalUtil.txRun(() -> selectDirectionsNotEqualUpdate
                .fetchStream()
                .map(mapper)
                .collect(Collectors.toList()));

        mvc.perform(TestUtil.put(BASE_URL + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                .andExpect(status().isNoContent());

        final List<Direction> directionsAfterUpdate = transactionalUtil.txRun(() -> selectDirectionsNotEqualUpdate
                .fetchStream()
                .map(mapper)
                .collect(Collectors.toList()));

        directionsBeforeUpdate.forEach(x -> {
            Optional<Direction> direction = directionsAfterUpdate.stream()
                    .filter(x1 -> x1.getId().equals(x.getId()))
                    .findFirst();
            Assertions.assertThat(direction)
                    .isPresent()
                    .contains(x);
        });

        Assertions.assertThat(transactionalUtil.txRun(() -> directionRepository.findById(id)))
                .satisfies(direction -> {
                    Assertions.assertThat(direction.getName()).isNotBlank().isEqualTo(d.getName());
                    Assertions.assertThat(direction.getLeadId()).isNotNull().isEqualTo(d.getLeadId());
                    Assertions.assertThat(direction.getLeadName()).isNotBlank().isEqualTo(d.getLeadName());
                });
    }

    @ParameterizedTest
    @MethodSource("updateFailedArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/direction/data.sql")
    void updateFailed(final UUID id, final String request, final String response, final HttpStatus status) {
        final SelectWhereStep<Record> selectDirectionsNotEqualUpdate = dsl.selectFrom(DIRECTION);

        final Function<Record, Direction> mapper = record -> new Direction()
                .setId(record.get(DIRECTION.ID))
                .setName(record.get(DIRECTION.NAME))
                .setLeadId(record.get(DIRECTION.LEAD_ID));

        final List<Direction> directionsBeforeUpdate = transactionalUtil.txRun(() -> selectDirectionsNotEqualUpdate
                .fetchStream()
                .map(mapper)
                .collect(Collectors.toList()));

        mvc.perform(put(BASE_URL + id)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                .andExpect(status().is(status.value()))
                .andExpect(content().json(response, true));

        final List<Direction> directionsAfterUpdate = transactionalUtil.txRun(() -> selectDirectionsNotEqualUpdate
                .fetchStream()
                .map(mapper)
                .collect(Collectors.toList()));

        directionsBeforeUpdate.forEach(x -> {
            Optional<Direction> direction = directionsAfterUpdate.stream()
                    .filter(x1 -> x1.getId().equals(x.getId()))
                    .findFirst();
            Assertions.assertThat(direction)
                    .isPresent()
                    .contains(x);
        });
    }

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/direction/data.sql")
    void deleteSuccess() {
        final UUID id = UUID.fromString("02f27e01-2ac4-4518-9c66-d0b7e2259a6f");

        mvc.perform(delete(BASE_URL + id))
                .andExpect(status().isNoContent());

        Assertions.assertThat(transactionalUtil.txRun(() -> dsl.fetchExists(DIRECTION, DIRECTION.ID.eq(id))))
                .isFalse();
        Assertions.assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(DIRECTION)))
                .isOne();
    }
}
