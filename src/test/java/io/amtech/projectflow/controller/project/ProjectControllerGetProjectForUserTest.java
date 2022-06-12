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
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerGetProjectForUserTest extends AbstractMvcTest {
    static Stream<Arguments> getProjectForUserArgs() {
        return Stream.of(
                Arguments.arguments("/json/ProjectControllerGetProjectForUserTest/getProjectForUser/request/auth_director.json",
                                    "/json/ProjectControllerGetProjectForUserTest/getProjectForUser/response/director_response.json"),
                Arguments.arguments("/json/ProjectControllerGetProjectForUserTest/getProjectForUser/request/auth_direction_lead.json",
                                    "/json/ProjectControllerGetProjectForUserTest/getProjectForUser/response/direction_lead_response.json"),
                Arguments.arguments("/json/ProjectControllerGetProjectForUserTest/getProjectForUser/request/project_lead_auth.json",
                                    "/json/ProjectControllerGetProjectForUserTest/getProjectForUser/response/project_lead_response.json")
        );
    }

    @ParameterizedTest
    @MethodSource("getProjectForUserArgs")
    @Sql(scripts = {
            "classpath:db/project/data_with_user.sql"
    })
    @SneakyThrows
    void getProjectForUser(final String pathToAuthRequest, final String pathToResponse) {
        final String authRequest = readContentFromClassPathResource(pathToAuthRequest);
        final String accessToken = getAccessToken(authRequest);

        final String response = readContentFromClassPathResource(pathToResponse);
        authMvc.perform(get("/projects/profile")
                                .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }
}