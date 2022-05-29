package io.amtech.projectflow.controller.project;

import io.amtech.projectflow.test.base.AbstractMvcTest;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.jdbc.Sql;

import java.util.stream.Stream;

import static io.amtech.projectflow.test.util.TestUtil.get;
import static io.amtech.projectflow.test.util.TestUtil.readContentFromClassPathResource;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerDetailTest extends AbstractMvcTest {

    static Stream<Arguments> getDetailArgs() {
        return Stream.of(
                Arguments.arguments("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f", "/json/ProjectControllerDetailTest/getDetail/positive_case_first_project.json"),
                Arguments.arguments("4e7efeef-553f-4996-bc03-1c0925d56946", "/json/ProjectControllerDetailTest/getDetail/positive_case_second_project.json")
        );
    }

    @ParameterizedTest
    @MethodSource("getDetailArgs")
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/project/milestone/data.sql",
            "classpath:db/project/milestone/add_milestones.sql"
    })
    void getDetail(final String id, final String pathToResponse) {
        final String response = readContentFromClassPathResource(pathToResponse);

        mvc.perform(get("/projects/detail/" + id))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }
}
