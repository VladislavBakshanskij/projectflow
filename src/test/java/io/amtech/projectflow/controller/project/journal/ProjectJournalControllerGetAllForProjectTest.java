package io.amtech.projectflow.controller.project.journal;

import io.amtech.projectflow.test.base.AbstractMvcTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static io.amtech.projectflow.test.util.TestUtil.get;
import static io.amtech.projectflow.test.util.TestUtil.readContentFromClassPathResource;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProjectJournalControllerGetAllForProjectTest extends AbstractMvcTest {
    @Test
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/project/journal/data.sql"
    })
    void getSuccess() {
        final String response = readContentFromClassPathResource("/json/ProjectJournalControllerGetAllForProjectTest/getSuccess/response/positive_case.json");
        mvc.perform(get("/projects/ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f/journals")
                            .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }

    @Test
    @SneakyThrows
    void getFailedProjectNotFound() {
        final String response = readContentFromClassPathResource("/json/ProjectJournalControllerGetAllForProjectTest/getFailedProjectNotFound/response/project_not_found.json");
        mvc.perform(get("/projects/ffd2f49a-0000-0000-0000-94d47c05ab5f/journals")
                            .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response, true));
    }
}