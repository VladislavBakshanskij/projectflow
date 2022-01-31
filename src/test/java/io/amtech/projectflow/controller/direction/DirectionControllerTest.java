package io.amtech.projectflow.controller.direction;

import io.amtech.projectflow.test.base.AbstractMvcTest;
import io.amtech.projectflow.test.util.TestUtil;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;
import java.util.stream.Stream;

import static io.amtech.projectflow.jooq.tables.Direction.DIRECTION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class DirectionControllerTest extends AbstractMvcTest {
    private static final String BASE_URL = "/directions/";

    @Autowired
    private DSLContext dsl;

    static Stream<Arguments> createSuccessArgs() {
        return Stream.of(
                Arguments.arguments(readJson(""), readJson(""))
        );
    }

    @ParameterizedTest
    @MethodSource("createSuccessArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/direction/data.sql")
    void createSuccess(final String request, final String response) {
        mvc.perform(TestUtil.post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(content().json(response, false));
    }

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/direction/data.sql")
    void getSuccess() {
        final UUID id = UUID.fromString("");
        final String response = readJson("", id);
        mvc.perform(TestUtil.get(BASE_URL + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }

    static Stream<Arguments> updateSuccessArgs() {
        return Stream.of(
                Arguments.arguments("")
        );
    }

    @ParameterizedTest
    @MethodSource("updateSuccessArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/direction/data.sql")
    void updateSuccess(final String request) {
        // todo add check change state
        mvc.perform(TestUtil.put(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/direction/data.sql")
    void deleteSuccess() {
        final UUID id = UUID.fromString("");

        mvc.perform(TestUtil.delete(BASE_URL + id))
                .andExpect(status().isNoContent());

        Assertions.assertThat(transactionalUtil.txRun(() -> dsl.fetchExists(DIRECTION, DIRECTION.ID.eq(id))))
                .isFalse();
        Assertions.assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(DIRECTION)))
                .isZero();
    }

    private static String readJson(final String path, final Object... args) {
        final String content = TestUtil.readContentFromClassPathResource(path);
        return String.format(content, args);
    }
}