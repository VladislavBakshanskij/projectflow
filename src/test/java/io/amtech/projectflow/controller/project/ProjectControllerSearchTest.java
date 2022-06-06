package io.amtech.projectflow.controller.project;

import io.amtech.projectflow.test.util.TestUtil;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static io.amtech.projectflow.test.util.TestUtil.readContentFromClassPathResource;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerSearchTest extends AbstractProjectMvcTest {
    private static final String BASE_URL = "/projects/";

    static Stream<Arguments> searchSuccessArgs() {
        return Stream.of(
                Arguments.arguments("",
                                    readJson("searchSuccess/all.json")),
                Arguments.arguments("?createDateFrom=2021-07-09T10:49:03.839234Z&createDateTo=2021-12-09T10:49:03.839234Z",
                                    readJson("searchSuccess/create_date_from_and_create_date_to.json")),
                Arguments.arguments("?createDateFrom=2021-07-09T10:49:03.839234Z&status=ON_PL_PLANNING",
                                    readJson("searchSuccess/create_date_from_and_status.json")),
                Arguments.arguments("?directionId=2f7c06e7-006a-4ca6-98e9-2ba13a0af6c7&projectLeadId=7a1d3076-6d80-4a66-b50e-2ed5c9ee056c",
                                    readJson("searchSuccess/direction_id_and_project_lead_id.json")),
                Arguments.arguments("?name=w&status=UNAPPROVED&projectLeadId=7a1d3076-6d80-4a66-b50e-2ed5c9ee056c",
                                    readJson("searchSuccess/name_and_status_and_project_lead_id.json"))
        );
    }

    private static String readJson(final String resource, final Object... args) {
        final String content = readContentFromClassPathResource("json/ProjectControllerSearchTest/" + resource);
        return String.format(content, args);
    }

    @ParameterizedTest
    @MethodSource("searchSuccessArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/search/data.sql")
    void searchSuccess(final String url, final String response) {
        mvc.perform(TestUtil.get(BASE_URL + url))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/search/data.sql")
    void searchFailed() {
        final String response = readJson("searchFailed/status_not_found.json");

        mvc.perform(TestUtil.get(BASE_URL + "?status=NOT_FOUND"))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response, true));
    }
}
