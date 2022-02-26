package io.amtech.projectflow.controller.project.journal;

import io.amtech.projectflow.test.base.AbstractMvcTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;
import java.util.stream.Stream;

import static io.amtech.projectflow.test.util.TestUtil.get;
import static io.amtech.projectflow.test.util.TestUtil.readContentFromClassPathResource;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectJournalControllerTest extends AbstractMvcTest {
    private static final String BASE_URL = "/projects/%s/journals/";

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/journal/data.sql")
    void getSuccess() {
        final UUID projectId = UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f");
        final UUID journalId = UUID.fromString("b0dccdfd-7cd6-47d0-a5c5-6aa0379fb394");

        final String response = readJson("getSuccess/response/positive_case.json");
        mvc.perform(get(String.format(BASE_URL, projectId) + journalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }

    static Stream<Arguments> getFailedArgs() {
        return Stream.of(
                Arguments.arguments(UUID.randomUUID(),
                        UUID.fromString("b0dccdfd-7cd6-47d0-a5c5-6aa0379fb394"),
                        readJson("getFailed/response/negative_case_project_not_found.json")),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                        UUID.randomUUID(),
                        readJson("getFailed/response/negative_case_journal_not_found.json"))
        );
    }

    @ParameterizedTest
    @MethodSource("getFailedArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/journal/data.sql")
    void getFailed(final UUID projectId, final UUID journalId, final String response) {
        mvc.perform(get(String.format(BASE_URL, projectId) + journalId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response, true));
    }

    private static String readJson(final String path, final Object... args) {
        final String content = readContentFromClassPathResource("/json/ProjectJournalControllerTest/" + path);
        return String.format(content, args);
    }
}
