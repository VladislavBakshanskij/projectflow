package io.amtech.projectflow.controller.project;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static io.amtech.projectflow.test.util.TestUtil.post;
import static io.amtech.projectflow.test.util.TestUtil.readContentFromClassPathResource;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectControllerFailedAuthCreateTest extends AbstractProjectMvcTest {
    @Test
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/users/data.sql"
    })
    void createFailedUserNotDirector() {
        final String request = readContentFromClassPathResource("/json/ProjectControllerForbiddenCreateTest/createFailedUserNotDirector/request/positive_case.json");
        final String response = readContentFromClassPathResource("/json/ProjectControllerForbiddenCreateTest/createFailedUserNotDirector/response/forbidden.json");
        authMvc.perform(post("/projects")
                                .header(AUTHORIZATION, "Bearer " + getProjectLeadAccessToken())
                                .contentType(APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isForbidden())
                .andExpect(content().json(response, true));
    }

    @Test
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/users/data.sql"
    })
    void createFailedTokenTypeIsInvalid() {
        final String request = readContentFromClassPathResource("/json/ProjectControllerForbiddenCreateTest/createFailedTokenTypeIsInvalid/request/positive_case.json");
        final String response = readContentFromClassPathResource("/json/ProjectControllerForbiddenCreateTest/createFailedTokenTypeIsInvalid/response/token_type_is_invalid.json");
        authMvc.perform(post("/projects")
                                .header(AUTHORIZATION, getDirectorAccessToken())
                                .contentType(APPLICATION_JSON)
                                .content(request))
                .andExpect(status().isUnauthorized())
                .andExpect(content().json(response, true));
    }

}
