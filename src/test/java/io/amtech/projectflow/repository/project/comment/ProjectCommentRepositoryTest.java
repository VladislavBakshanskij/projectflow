package io.amtech.projectflow.repository.project.comment;

import io.amtech.projectflow.model.project.ProjectComment;
import io.amtech.projectflow.test.base.AbstractIntegrationTest;
import lombok.SneakyThrows;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.UUID;

import static io.amtech.projectflow.jooq.tables.ProjectComment.PROJECT_COMMENT;
import static org.assertj.core.api.Assertions.assertThat;

class ProjectCommentRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private DSLContext dsl;

    @Autowired
    private ProjectCommentRepository projectCommentRepository;

    @Test
    @Sql(scripts = {
            "classpath:db/project/data.sql"
    })
    @SneakyThrows
    void saveSuccess() {
        final UUID projectId = UUID.fromString("4e7efeef-553f-4996-bc03-1c0925d56946");
        final ProjectComment projectCommentToSave = new ProjectComment()
                .setProjectId(projectId)
                .setMessage("this is better project")
                .setLogin("vlad")
                .setCreateDate(Instant.now());

        transactionalUtil.txRun(() -> {
            assertThat(projectCommentRepository.save(projectCommentToSave))
                    .isNotNull()
                    .satisfies(savedComment -> {
                        assertThat(savedComment.getId()).isNotNull();
                        assertThat(savedComment.getProjectId()).isNotNull().isEqualTo(projectCommentToSave.getProjectId());
                        assertThat(savedComment.getMessage()).isNotBlank().isEqualTo(projectCommentToSave.getMessage());
                        assertThat(savedComment.getLogin()).isNotBlank().isEqualTo(projectCommentToSave.getLogin());
                        assertThat(savedComment.getCreateDate()).isNotNull().isEqualTo(projectCommentToSave.getCreateDate());
                    });

            assertThat(dsl.fetchCount(PROJECT_COMMENT)).isOne();
        });
    }
}