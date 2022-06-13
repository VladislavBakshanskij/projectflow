package io.amtech.projectflow.controller.project.flow;

import io.amtech.projectflow.model.project.ProjectStatus;
import io.amtech.projectflow.repository.project.ProjectRepository;
import io.amtech.projectflow.test.base.AbstractMvcTest;
import lombok.SneakyThrows;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;
import java.util.stream.Stream;

import static io.amtech.projectflow.test.util.TestUtil.patch;
import static io.amtech.projectflow.test.util.TestUtil.readContentFromClassPathResource;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectFlowControllerTest extends AbstractMvcTest {
    @Autowired
    private ProjectRepository projectRepository;

    static Stream<Arguments> changeStatusSuccessArgs() {
        return Stream.of(
                Arguments.arguments("/json/ProjectFlowControllerTest/changeStatusSuccess/request/project_lead_auth.json",
                                    "ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f", ProjectStatus.ON_PL_PLANNING),
                Arguments.arguments("/json/ProjectFlowControllerTest/changeStatusSuccess/request/project_lead_auth.json",
                                    "4e7efeef-553f-4996-bc03-1c0925d56946", ProjectStatus.ON_DL_APPROVING),
                Arguments.arguments("/json/ProjectFlowControllerTest/changeStatusSuccess/request/direction_lead_auth.json",
                                    "4e7efeef-553f-4996-bc03-1c0925d56926", ProjectStatus.ON_DIRECTOR_APPROVING),
                Arguments.arguments("/json/ProjectFlowControllerTest/changeStatusSuccess/request/director_auth.json",
                                    "4e7efeef-553f-4996-bc03-1c0925d56936", ProjectStatus.DIRECTOR_APPROVED),
                Arguments.arguments("/json/ProjectFlowControllerTest/changeStatusSuccess/request/director_auth.json",
                                    "4e7efeef-553f-4996-bc03-1b0925d56946", ProjectStatus.DONE)
        );
    }

    @ParameterizedTest
    @MethodSource("changeStatusSuccessArgs")
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/project/flow/data.sql"
    })
    void changeStatusSuccess(final String pathToAuthRequest, final String projectId, final ProjectStatus expectedStatus) {
        final String authRequest = readContentFromClassPathResource(pathToAuthRequest);
        final String accessToken = getAccessToken(authRequest);

        final String response = readContentFromClassPathResource("/json/ProjectFlowControllerTest/changeStatusSuccess/response/positive_case.json");
        authMvc.perform(patch(String.format("/projects/%s/flow", projectId))
                                .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));

        transactionalUtil.txRun(() -> {
            assertThat(projectRepository.findById(UUID.fromString(projectId)))
                    .isNotNull()
                    .satisfies(project -> assertThat(project.getStatus()).isNotNull().isEqualTo(expectedStatus));
        });
    }


    static Stream<Arguments> rollbackStatusSuccessArgs() {
        return Stream.of(
                Arguments.arguments("/json/ProjectFlowControllerTest/rollbackStatusSuccess/request/direction_lead_auth.json",
                                    "4e7efeef-553f-4996-bc03-1c0925d56926", ProjectStatus.ON_PL_PLANNING),
                Arguments.arguments("/json/ProjectFlowControllerTest/rollbackStatusSuccess/request/director_auth.json",
                                    "4e7efeef-553f-4996-bc03-1c0925d56936", ProjectStatus.ON_DL_APPROVING),
                Arguments.arguments("/json/ProjectFlowControllerTest/rollbackStatusSuccess/request/director_auth.json",
                                    "4e7efeef-553f-4996-bc03-1b0925d56946", ProjectStatus.ON_PL_PLANNING)
        );
    }

    @ParameterizedTest
    @MethodSource("rollbackStatusSuccessArgs")
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/project/flow/data.sql"
    })
    void rollbackStatusSuccess(final String pathToAuthRequest, final String projectId, final ProjectStatus expectedStatus) {
        final String authRequest = readContentFromClassPathResource(pathToAuthRequest);
        final String accessToken = getAccessToken(authRequest);

        final String response = readContentFromClassPathResource("/json/ProjectFlowControllerTest/changeStatusSuccess/response/positive_case.json");
        authMvc.perform(patch(String.format("/projects/%s/flow/rollback", projectId))
                                .header(AUTHORIZATION, "Bearer " + accessToken))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));

        transactionalUtil.txRun(() -> {
            assertThat(projectRepository.findById(UUID.fromString(projectId)))
                    .isNotNull()
                    .satisfies(project -> assertThat(project.getStatus()).isNotNull().isEqualTo(expectedStatus));
        });
    }
}