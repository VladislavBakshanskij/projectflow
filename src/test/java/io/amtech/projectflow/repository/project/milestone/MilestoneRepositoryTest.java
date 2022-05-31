package io.amtech.projectflow.repository.project.milestone;


import io.amtech.projectflow.model.project.milestone.Milestone;
import io.amtech.projectflow.repository.project.milesone.MilestoneRepository;
import io.amtech.projectflow.test.base.AbstractIntegrationTest;
import lombok.SneakyThrows;
import org.jooq.DSLContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static io.amtech.projectflow.jooq.Tables.MILESTONE;
import static org.assertj.core.api.Assertions.assertThat;

class MilestoneRepositoryTest extends AbstractIntegrationTest {
    @Autowired
    private DSLContext dsl;

    @Autowired
    private MilestoneRepository milestoneRepository;

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/milestone/data.sql")
    void saveSuccess() {
        final Instant now = Instant.now();
        final Milestone milestone = new Milestone()
                .setName("super feature")
                .setDescription("some desc")
                .setProjectId(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"))
                .setFactStartDate(now.minus(7, ChronoUnit.DAYS))
                .setFactFinishDate(now.plus(7, ChronoUnit.DAYS))
                .setPlannedStartDate(now.minus(30, ChronoUnit.DAYS))
                .setPlannedFinishDate(now.plus(30, ChronoUnit.DAYS))
                .setProgressPercent((short) 50);

        transactionalUtil.txRun(() -> {
            assertThat(milestoneRepository.save(milestone))
                    .satisfies(saved -> {
                        assertThat(saved.getId()).isNotNull();
                        assertThat(saved.getName()).isNotBlank().isEqualTo(milestone.getName());
                        assertThat(saved.getDescription()).isNotBlank().isEqualTo(milestone.getDescription());
                        assertThat(saved.getProjectId()).isNotNull().isEqualTo(milestone.getProjectId());
                        assertThat(saved.getProgressPercent()).isEqualTo(milestone.getProgressPercent());
                        assertThat(saved.getPlannedStartDate()).isNotNull().isEqualTo(milestone.getPlannedStartDate());
                        assertThat(saved.getPlannedFinishDate()).isNotNull().isEqualTo(milestone.getPlannedFinishDate());
                        assertThat(saved.getFactStartDate()).isNotNull().isEqualTo(milestone.getFactStartDate());
                        assertThat(saved.getFactFinishDate()).isNotNull().isEqualTo(milestone.getFactFinishDate());
                    });
        });

        assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(MILESTONE))).isEqualTo(3);
    }

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/milestone/data.sql")
    void getSuccess() {
        final UUID id = UUID.fromString("fda0c0f6-b637-4039-a2d6-640aba6b4e46");
        final UUID projectId = UUID.fromString("4e7efeef-553f-4996-bc03-1c0925d56946");

        transactionalUtil.txRun(() -> {
            assertThat(milestoneRepository.findByIdWithProject(projectId, id))
                    .isNotNull()
                    .satisfies(milestone -> {
                        assertThat(milestone.getId()).isEqualTo(id);
                        assertThat(milestone.getName()).isNotBlank().isEqualTo("do site");
                        assertThat(milestone.getDescription()).isNotBlank().isEqualTo("do site on react");
                        assertThat(milestone.getProjectId()).isNotNull().isEqualTo(UUID.fromString("4e7efeef-553f-4996-bc03-1c0925d56946"));
                        assertThat(milestone.getProgressPercent()).isZero();
                        assertThat(milestone.getPlannedStartDate()).isNotNull().isEqualTo(Instant.parse("2021-02-09T11:49:03.839234Z"));
                        assertThat(milestone.getPlannedFinishDate()).isNotNull().isEqualTo(Instant.parse("2021-06-09T11:49:03.839234Z"));
                        assertThat(milestone.getFactStartDate()).isNotNull().isEqualTo(Instant.parse("2021-03-09T11:49:03.839234Z"));
                        assertThat(milestone.getFactFinishDate()).isNotNull().isEqualTo(Instant.parse("2021-06-09T11:49:03.839234Z"));
                    });
        });
    }
}
