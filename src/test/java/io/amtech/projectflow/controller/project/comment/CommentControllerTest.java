package io.amtech.projectflow.controller.project.comment;

import io.amtech.projectflow.test.base.AbstractMvcTest;
import lombok.SneakyThrows;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static io.amtech.projectflow.jooq.tables.ProjectComment.PROJECT_COMMENT;
import static io.amtech.projectflow.test.util.TestUtil.*;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CommentControllerTest extends AbstractMvcTest {
    @Autowired
    private DSLContext dsl;

    static Stream<Arguments> createSuccessArgs() {
        return Stream.of(
                Arguments.arguments("4e7efeef-553f-4996-bc03-1c0925d56946",
                                    readContentFromClassPathResource("/json/CommentControllerTest/createSuccess/request/positive_case_4e7efeef-553f-4996-bc03-1c0925d56946.json"),
                                    readContentFromClassPathResource("/json/CommentControllerTest/createSuccess/response/positive_case_4e7efeef-553f-4996-bc03-1c0925d56946.json")),
                Arguments.arguments("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f",
                                    readContentFromClassPathResource("/json/CommentControllerTest/createSuccess/request/positive_case_ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f.json"),
                                    readContentFromClassPathResource("/json/CommentControllerTest/createSuccess/response/positive_case_ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f.json"))
        );
    }

    static Stream<Arguments> createFailedArgs() {
        return Stream.of(
                Arguments.arguments("ffd2f49a-0000-0000-0000-94d47c05ab5f",
                                    HttpStatus.NOT_FOUND,
                                    readContentFromClassPathResource("/json/CommentControllerTest/createFailed/request/positive_case.json"),
                                    readContentFromClassPathResource("/json/CommentControllerTest/createFailed/response/project_not_found.json")),
                Arguments.arguments("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f",
                                    HttpStatus.BAD_REQUEST,
                                    readContentFromClassPathResource("/json/CommentControllerTest/createFailed/request/message_is_empty.json"),
                                    readContentFromClassPathResource("/json/CommentControllerTest/createFailed/response/message_must_not_be_empty.json")),
                Arguments.arguments("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f",
                                    HttpStatus.BAD_REQUEST,
                                    readContentFromClassPathResource("/json/CommentControllerTest/createFailed/request/message_has_length_more_5000.json"),
                                    readContentFromClassPathResource("/json/CommentControllerTest/createFailed/response/message_has_length_more_5000.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("createSuccessArgs")
    @Sql(scripts = {
            "classpath:db/project/comment/data.sql"
    })
    @SneakyThrows
    void createSuccess(final String projectId, final String request, final String response) {
        final String token = getAuthToken();

        authMvc.perform(post("/projects/" + projectId + "/comments")
                                .contentType(APPLICATION_JSON)
                                .content(request)
                                .header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createDate").exists())
                .andExpect(content().json(response, false));

        transactionalUtil.txRun(() -> assertThat(dsl.fetchCount(PROJECT_COMMENT)).isOne());
    }

    @Test
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/project/comment/data.sql"
    })
    void createFailedNotAuth() {
        final String request = readContentFromClassPathResource("/json/CommentControllerTest/createFailedNotAuth/request/positive_case.json");
        final String response = readContentFromClassPathResource("/json/CommentControllerTest/createFailedNotAuth/response/not_auth.json");
        mvc.perform(post("/projects/ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f/comments")
                            .contentType(APPLICATION_JSON)
                            .content(request))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(response, true));
    }

    @ParameterizedTest
    @MethodSource("createFailedArgs")
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/project/comment/data.sql"
    })
    void createFailed(final String projectId, final HttpStatus status, final String request, final String response) {
        final String token = getAuthToken();

        authMvc.perform(post("/projects/" + projectId + "/comments")
                                .contentType(APPLICATION_JSON)
                                .content(request)
                                .header(AUTHORIZATION, "Bearer " + token))
                .andExpect(status().is(status.value()))
                .andExpect(content().json(response, true));


        transactionalUtil.txRun(() -> assertThat(dsl.fetchCount(PROJECT_COMMENT)).isZero());
    }

    @SneakyThrows
    private String getAuthToken() {
        final String request = readContentFromClassPathResource("/json/common/auth/request/positive_case.json");
        final String response = authMvc.perform(post("/auth/login")
                                                        .contentType(APPLICATION_JSON)
                                                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.access").exists())
                .andExpect(jsonPath("$.refresh").exists())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return convertJsonToMap(response)
                .getOrDefault("access", EMPTY)
                .toString();
    }
}