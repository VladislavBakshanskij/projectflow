package io.amtech.projectflow.controller.project;

import lombok.SneakyThrows;
import org.jooq.DSLContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;
import java.util.stream.Stream;

import static io.amtech.projectflow.jooq.tables.Project.PROJECT;
import static io.amtech.projectflow.test.util.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProjectControllerTest extends AbstractProjectMvcTest {
    private static final String BASE_URL = "/projects/";
    private static final String BASE_ID_URL = BASE_URL + "%s";

    @Autowired
    private DSLContext dsl;

    static Stream<Arguments> createSuccessArgs() {
        return Stream.of(
                Arguments.arguments(readJson("createSuccess/request/positive_case.json"),
                                    readJson("createSuccess/response/positive_case.json")),
                Arguments.arguments(readJson("createSuccess/request/positive_case_without_description.json"),
                                    readJson("createSuccess/response/positive_case_without_description.json"))
        );
    }

    static Stream<Arguments> createFailedArgs() {
        return Stream.of(
                Arguments.arguments(readJson("createFailed/request/name_is_missing.json"),
                                    readJson("createFailed/response/name_is_missing.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments(readJson("createFailed/request/description_more_2048.json"),
                                    readJson("createFailed/response/description_more_2048.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments(readJson("createFailed/request/direction_is_missing.json"),
                                    readJson("createFailed/response/direction_is_missing.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments(readJson("createFailed/request/projectLead_is_missing.json"),
                                    readJson("createFailed/response/projectLead_is_missing.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments(readJson("createFailed/request/projectLead_not_found.json"),
                                    readJson("createFailed/response/projectLead_not_found.json"),
                                    HttpStatus.NOT_FOUND),
                Arguments.arguments(readJson("createFailed/request/direction_not_found.json"),
                                    readJson("createFailed/response/direction_not_found.json"),
                                    HttpStatus.NOT_FOUND));
    }

    static Stream<Arguments> updateSuccessArgs() {
        return Stream.of(
                Arguments.arguments("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f",
                                    readJson("updateSuccess/request/update_full_request.json"))
        );
    }

    static Stream<Arguments> updateFailedArgs() {
        return Stream.of(
                Arguments.arguments("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f",
                                    readJson("updateFailed/request/name_is_null.json"),
                                    readJson("updateFailed/response/name_is_null.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f",
                                    readJson("updateFailed/request/only_name.json"),
                                    readJson("updateFailed/response/only_name.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f",
                                    readJson("updateFailed/request/only_leadId.json"),
                                    readJson("updateFailed/response/only_leadId.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f",
                                    readJson("updateFailed/request/projectLead_not_found.json"),
                                    readJson("updateFailed/response/projectLead_not_found.json"),
                                    HttpStatus.NOT_FOUND),
                Arguments.arguments("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f",
                                    readJson("updateFailed/request/direction_not_found.json"),
                                    readJson("updateFailed/response/direction_not_found.json"),
                                    HttpStatus.NOT_FOUND),
                Arguments.arguments("787cbb77-f0c7-407f-ad41-b63f0b45b5e3",
                                    readJson("updateFailed/request/project_not_found.json"),
                                    readJson("updateFailed/response/project_not_found.json"),
                                    HttpStatus.NOT_FOUND));
    }

    static Stream<Arguments> deleteSuccessArgs() {
        return Stream.of(Arguments.arguments("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"));
    }

    static Stream<Arguments> deleteFailedArgs() {
        return Stream.of(
                Arguments.arguments("1f6c9f10-6aac-47ec-b378-be96bf5df1c1",
                                    readJson("deleteFailed/response/first_project_not_found.json"), HttpStatus.NOT_FOUND),
                Arguments.arguments("a2d07d5e-fe34-4b07-b182-76f9362ad489",
                                    readJson("deleteFailed/response/second_project_not_found.json"), HttpStatus.NOT_FOUND));
    }

    static Stream<Arguments> getSuccessArgs() {
        return Stream.of(
                Arguments.arguments("4e7efeef-553f-4996-bc03-1c0925d56946",
                                    readJson("getSuccess/response/positive_case.json"),
                                    HttpStatus.OK)
        );
    }

    static Stream<Arguments> getFailedArgs() {
        return Stream.of(
                Arguments.arguments("0e551e6b-af6c-4999-a0ef-5997902b1474",
                                    readJson("getFailed/response/project_not_found.json")));
    }

    private static String readJson(final String resource, Object... args) {
        String template = readContentFromClassPathResource("json/ProjectControllerTest/" + resource);

        return String.format(template, args);
    }

    @ParameterizedTest
    @MethodSource("createSuccessArgs")
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/auth/users.sql",
            "classpath:db/project/data.sql"
    })
    void createSuccess(final String request, final String response) {
        authMvc.perform(post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getDirectorAccessToken())
                                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createDate").exists())
                .andExpect(content().json(response, false));

        transactionalUtil.txRun(() -> {
            assertThat(dsl.fetchCount(PROJECT))
                    .isEqualTo(3);
        });
    }

    @ParameterizedTest
    @MethodSource("createFailedArgs")
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/auth/users.sql",
            "classpath:db/project/data.sql"
    })
    void createFailed(final String request, final String response, final HttpStatus status) {
        authMvc.perform(post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getDirectorAccessToken())
                                .content(request))
                .andExpect(status().is(status.value()))
                .andExpect(content().json(response, true));
    }

    @ParameterizedTest
    @MethodSource("updateSuccessArgs")
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/auth/users.sql",
            "classpath:db/project/data.sql"
    })
    void updateSuccess(final String id, final String request) {
        authMvc.perform(put(String.format(BASE_ID_URL, id))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getDirectorAccessToken())
                                .content(request))
                .andExpect(status().isFound());
    }

    @ParameterizedTest
    @MethodSource("updateFailedArgs")
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/auth/users.sql",
            "classpath:db/project/data.sql"
    })
    void updateFailTest(final String id, final String request, final String response, final HttpStatus status) {
        authMvc.perform(put(String.format(BASE_ID_URL, id))
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getDirectorAccessToken())
                                .content(request))
                .andExpect(status().is(status.value()))
                .andExpect(content().json(response, true));
    }

    @ParameterizedTest
    @MethodSource("deleteSuccessArgs")
    @SneakyThrows
    @Sql("classpath:db/project/data.sql")
    void deleteSuccess(final String id) {
        mvc.perform(delete(String.format(BASE_ID_URL, id)))
                .andExpect(status().isNoContent());

        assertThat(transactionalUtil.txRun(() -> dsl.fetchExists(PROJECT, PROJECT.ID.eq(UUID.fromString(id)))))
                .isFalse();
        assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(PROJECT)))
                .isOne();
    }

    @ParameterizedTest
    @MethodSource("deleteFailedArgs")
    @SneakyThrows
    @Sql("classpath:db/project/data.sql")
    void deleteFailed(final String id, final String response, final HttpStatus status) {
        mvc.perform(delete(String.format(BASE_ID_URL, id)))
                .andExpect(status().is(status.value()))
                .andExpect(content().json(response, true));
    }

    @ParameterizedTest
    @MethodSource("getSuccessArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/data.sql")
    void getSuccess(final String id, final String response, final HttpStatus status) {
        mvc.perform(get(String.format(BASE_ID_URL, id)))
                .andExpect(status().is(status.value()))
                .andExpect(content().json(response, true));
    }

    @ParameterizedTest
    @MethodSource("getFailedArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/data.sql")
    void getFailed(final String id, final String response) {
        mvc.perform(get(String.format(BASE_ID_URL, id)))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response, true));
    }
}
