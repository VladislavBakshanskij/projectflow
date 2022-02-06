package io.amtech.projectflow.repository.project;

import io.amtech.projectflow.model.Project;
import io.amtech.projectflow.model.ProjectStatus;
import io.amtech.projectflow.test.base.AbstractIntegrationTest;
import lombok.SneakyThrows;
import org.assertj.core.api.Assertions;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.util.UUID;

import static io.amtech.projectflow.jooq.tables.Project.PROJECT;

class ProjectRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private DSLContext dsl;

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/data.sql")
    void saveSuccess() {
        final Instant now = Instant.now();
        final Project project = new Project()
                .setName("future")
                .setDescription("future project")
                .setProjectLeadId(UUID.fromString("c631da58-8ae6-4a8d-bc6a-107c42a2b598"))
                .setDirectionId(UUID.fromString("caaad756-f11a-4635-898d-2861071ec38d"));

        Project savedProject = transactionalUtil.txRun(() -> projectRepository.save(project));

        Assertions.assertThat(savedProject)
                .isNotNull()
                .satisfies(p -> {
                    Assertions.assertThat(p.getId())
                            .isNotNull();
                    Assertions.assertThat(p.getName())
                            .isNotBlank()
                            .isEqualTo(project.getName());
                    Assertions.assertThat(p.getDescription())
                            .isNotBlank()
                            .isEqualTo(project.getDescription());
                    Assertions.assertThat(p.getStatus())
                            .isNotNull()
                            .isEqualTo(ProjectStatus.UNAPPROVED);
                    Assertions.assertThat(p.getProjectLeadId())
                            .isNotNull()
                            .isEqualTo(UUID.fromString("c631da58-8ae6-4a8d-bc6a-107c42a2b598"));
                    Assertions.assertThat(p.getDirectionId())
                            .isNotNull()
                            .isEqualTo(UUID.fromString("caaad756-f11a-4635-898d-2861071ec38d"));
                    Assertions.assertThat(p.getCreateDate())
                            .isNotNull()
                            .isAfter(now.minusSeconds(2));
                    Assertions.assertThat(p.getCreateDate())
                            .isNotNull()
                            .isBeforeOrEqualTo(now.plusSeconds(2));
                });


        Assertions.assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(PROJECT)))
                .isEqualTo(3);
    }

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/data.sql")
    void getSuccess() {
        final UUID id = UUID.fromString("4e7efeef-553f-4996-bc03-1c0925d56946");
        final Project project = new Project()
                .setId(id)
                .setName("Three")
                .setDescription("message")
                .setStatus(ProjectStatus.ON_PL_PLANNING)
                .setCreateDate(Instant.parse("2021-07-09T11:49:03.839234Z"))
                .setProjectLeadId(UUID.fromString("fc9632a7-66b4-4627-846c-a0e65533637c"))
                .setDirectionId(UUID.fromString("211ed887-adc4-41bb-a8c4-e41393bd8e69"));

        Assertions.assertThat(transactionalUtil.txRun(() -> projectRepository.findById(id)))
                .isNotNull()
                .isEqualTo(project);
    }
}
