package io.amtech.projectflow.repository.project.journal;

import io.amtech.projectflow.model.project.ProjectStatus;
import io.amtech.projectflow.test.base.AbstractIntegrationTest;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ProjectJournalRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private ProjectJournalRepository repository;

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/journal/data.sql")
    void getSuccess() {
        final UUID projectId = UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f");
        final UUID journalId = UUID.fromString("b0dccdfd-7cd6-47d0-a5c5-6aa0379fb394");

        transactionalUtil.txRun(() -> {
            assertThat(repository.findByIdWithProjectId(projectId, journalId))
                    .isNotNull()
                    .satisfies(journal -> {
                        assertThat(journal.getId()).isNotNull().isEqualTo(journalId);
                        assertThat(journal.getProjectId()).isNotNull().isEqualTo(projectId);
                        assertThat(journal.getLogin()).isNotBlank().isEqualTo("Igor");
                        assertThat(journal.getUpdateDate()).isNotNull().isEqualTo(Instant.parse("2021-06-09T11:49:05.839234Z"));
                        assertThat(journal.getCurrentState()).isNotNull()
                                .satisfies(state -> {
                                    assertThat(state.size()).isEqualTo(7);
                                    assertThat(state.get("id"))
                                            .isNotNull()
                                            .matches(o -> UUID.fromString(o.toString()).equals(projectId), projectId.toString());
                                    assertThat(state.get("name"))
                                            .isNotNull()
                                            .matches(o -> o.toString().equals("One"), "One");
                                    assertThat(state.get("project_lead_id"))
                                            .isNotNull()
                                            .matches(o -> UUID.fromString(o.toString()).equals(UUID.fromString("fc9632a7-66b4-4627-846c-a0e65533637c")),
                                                    "fc9632a7-66b4-4627-846c-a0e65533637c");
                                    assertThat(state.get("direction_id"))
                                            .isNotNull()
                                            .matches(o -> UUID.fromString(o.toString()).equals(UUID.fromString("caaad756-f11a-4635-898d-2861071ec38d")),
                                                    "caaad756-f11a-4635-898d-2861071ec38d");
                                    assertThat(state.get("description"))
                                            .isNotNull()
                                            .matches(o -> o.toString().equals("message 1"),
                                                    "message 1");
                                    assertThat(state.get("create_date"))
                                            .isNotNull()
                                            .matches(o -> Instant.parse(o.toString()).equals(Instant.parse("2021-06-09T11:49:03.839234Z")),
                                                    "2021-06-09T11:49:03.839234Z");
                                    assertThat(state.get("status"))
                                            .isNotNull()
                                            .matches(o -> ProjectStatus.of(o.toString()).equals(ProjectStatus.UNAPPROVED),
                                                    "UNAPPROVED");
                                });
                    });
        });
    }
}
