package io.amtech.projectflow.controller.project;

import io.amtech.projectflow.repository.project.journal.ProjectJournalRecordMapper;
import lombok.SneakyThrows;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static io.amtech.projectflow.jooq.tables.ProjectJournal.PROJECT_JOURNAL;
import static io.amtech.projectflow.test.util.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProjectControllerCheckWriteIntoJournalTest extends AbstractProjectMvcTest {
    private static final String BASE_URL = "/projects/";

    @Autowired
    private DSLContext dsl;

    @Autowired
    private ProjectJournalRecordMapper mapper;

    private static String readJson(final String resource, Object... args) {
        final String template = readContentFromClassPathResource("json/ProjectControllerCheckWriteIntoJournalTest/" + resource);
        return String.format(template, args);
    }

    @Test
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/auth/users.sql",
            "classpath:db/project/data.sql"
    })
    void createSuccess() {
        final String request = readJson("createSuccess/request/positive_case.json");
        final String response = readJson("createSuccess/response/positive_case.json");

        authMvc.perform(post(BASE_URL)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getDirectorAccessToken())
                                .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.createDate").exists())
                .andExpect(content().json(response, false));

        transactionalUtil.txRun(() -> {
            assertThat(dsl.selectFrom(PROJECT_JOURNAL).fetchStream().map(mapper::map))
                    .singleElement()
                    .satisfies(journal -> {
                        assertThat(journal.getId()).isNotNull();
                        assertThat(journal.getProjectId()).isNotNull();
                        assertThat(journal.getLogin()).isNotBlank().isEqualTo("user");
                        assertThat(journal.getUpdateDate()).isNotNull();
                        assertThat(journal.getCurrentState()).isNotNull();
                    });
        });
    }

    @Test
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/auth/users.sql",
            "classpath:db/project/data.sql"
    })
    void updateSuccess() {
        final String request = readJson("updateSuccess/request/update_full_request.json");
        final UUID id = UUID.fromString("4e7efeef-553f-4996-bc03-1c0925d56946");
        authMvc.perform(put(BASE_URL + id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .header(HttpHeaders.AUTHORIZATION, "Bearer " + getDirectorAccessToken())
                                .content(request))
                .andExpect(status().isFound());

        transactionalUtil.txRun(() -> {
            assertThat(dsl.selectFrom(PROJECT_JOURNAL).fetchStream().map(mapper::map))
                    .singleElement()
                    .satisfies(journal -> {
                        assertThat(journal.getId()).isNotNull();
                        assertThat(journal.getProjectId()).isNotNull().isEqualTo(id);
                        assertThat(journal.getLogin()).isNotBlank().isEqualTo("user");
                        assertThat(journal.getUpdateDate()).isNotNull();
                        assertThat(journal.getCurrentState()).isNotNull();
                    });
        });
    }
}
