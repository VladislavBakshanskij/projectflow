package io.amtech.projectflow.controller.project.milestone;

import io.amtech.projectflow.model.project.milestone.Milestone;
import io.amtech.projectflow.repository.project.milesone.MilestoneRepository;
import io.amtech.projectflow.test.base.AbstractMvcTest;
import lombok.SneakyThrows;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SelectConditionStep;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.amtech.projectflow.jooq.Tables.MILESTONE;
import static io.amtech.projectflow.repository.project.milesone.MilestoneRepositoryImpl.mapper;
import static io.amtech.projectflow.test.util.TestUtil.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MilestoneControllerTest extends AbstractMvcTest {
    private static final String BASE_URL = "/projects/%s/milestones/";

    @Autowired
    private DSLContext dsl;

    @Autowired
    private MilestoneRepository milestoneRepository;

    static Stream<Arguments> createSuccessArgs() {
        return Stream.of(
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    readJson("createSuccess/request/positive_case.json"),
                                    readJson("createSuccess/response/positive_case.json")),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    readJson("createSuccess/request/positive_case_with_description.json"),
                                    readJson("createSuccess/response/positive_case_with_description.json")),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    readJson("createSuccess/request/positive_case_without_fact_dates.json"),
                                    readJson("createSuccess/response/positive_case_without_fact_dates.json"))
        );
    }

    static Stream<Arguments> createFailedArgs() {
        return Stream.of(
                Arguments.arguments(UUID.fromString("95C9C729-5786-4D1E-B6D4-6D25FCD22829"),
                                    readJson("createFailed/request/negative_case_project_not_found.json"),
                                    readJson("createFailed/response/negative_case_project_not_found.json"),
                                    HttpStatus.NOT_FOUND),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    readJson("createFailed/request/negative_case_name_is_null.json"),
                                    readJson("createFailed/response/negative_case_name_is_null.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    readJson("createFailed/request/negative_case_name_is_empty.json"),
                                    readJson("createFailed/response/negative_case_name_is_empty.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    readJson("createFailed/request/negative_case_description_is_large.json"),
                                    readJson("createFailed/response/negative_case_description_is_large.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    readJson("createFailed/request/negative_case_all_dates_is_missing.json"),
                                    readJson("createFailed/response/negative_case_all_dates_is_missing.json"),
                                    HttpStatus.BAD_REQUEST)
        );
    }

    static Stream<Arguments> getSuccessArgs() {
        return Stream.of(
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    UUID.fromString("ce5ee15c-3afa-4649-9d68-451ad23f4cfd"),
                                    readJson("getSuccess/response/positive_case.json")),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    UUID.fromString("026ae239-66c5-4ac5-b37b-cf6ba3d3b10c"),
                                    readJson("getSuccess/response/positive_case_with_nullable_field.json"))
        );
    }

    static Stream<Arguments> getFailedArgs() {
        return Stream.of(
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    UUID.randomUUID(),
                                    readJson("getFailed/response/negative_case_milestone_not_found.json")),
                Arguments.arguments(UUID.randomUUID(),
                                    UUID.fromString("fda0c0f6-b637-4039-a2d6-640aba6b4e46"),
                                    readJson("getFailed/response/negative_case_project_not_found.json"))
        );
    }

    static Stream<Arguments> updateSuccessArgs() {
        return Stream.of(
                Arguments.arguments(UUID.fromString("4e7efeef-553f-4996-bc03-1c0925d56946"),
                                    UUID.fromString("fda0c0f6-b637-4039-a2d6-640aba6b4e46"),
                                    readJson("updateSuccess/request/positive_case_update_name_and_fact_dates.json"))
        );
    }

    static Stream<Arguments> updateFailedArgs() {
        return Stream.of(
                Arguments.arguments(UUID.randomUUID(),
                                    UUID.fromString("fda0c0f6-b637-4039-a2d6-640aba6b4e46"),
                                    readJson("updateFailed/request/negative_case_project_not_found.json"),
                                    readJson("updateFailed/response/negative_case_project_not_found.json"),
                                    HttpStatus.NOT_FOUND),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    UUID.randomUUID(),
                                    readJson("updateFailed/request/negative_case_milestone_not_found.json"),
                                    readJson("updateFailed/response/negative_case_milestone_not_found.json"),
                                    HttpStatus.NOT_FOUND),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    UUID.fromString("fda0c0f6-b637-4039-a2d6-640aba6b4e46"),
                                    readJson("updateFailed/request/negative_case_name_is_missing.json"),
                                    readJson("updateFailed/response/negative_case_name_is_missing.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    UUID.fromString("fda0c0f6-b637-4039-a2d6-640aba6b4e46"),
                                    readJson("updateFailed/request/negative_case_name_more_255_characters.json"),
                                    readJson("updateFailed/response/negative_case_name_more_255_characters.json"),
                                    HttpStatus.BAD_REQUEST)
        );
    }

    static Stream<Arguments> deleteFailedArgs() {
        return Stream.of(
                Arguments.arguments(UUID.randomUUID(),
                                    UUID.fromString("fda0c0f6-b637-4039-a2d6-640aba6b4e46"),
                                    readJson("deleteFailed/response/negative_case_project_not_found.json")),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    UUID.randomUUID(),
                                    readJson("deleteFailed/response/negative_case_milestone_not_found.json"))
        );
    }

    static Stream<Arguments> updateProgressSuccessArgs() {
        return Stream.of(
                Arguments.arguments(UUID.fromString("4e7efeef-553f-4996-bc03-1c0925d56946"),
                                    UUID.fromString("fda0c0f6-b637-4039-a2d6-640aba6b4e46"),
                                    readJson("updateProgressSuccess/request/positive_case.json"))
        );
    }

    static Stream<Arguments> updateProgressFailedArgs() {
        return Stream.of(
                Arguments.arguments(UUID.randomUUID(),
                                    UUID.fromString("fda0c0f6-b637-4039-a2d6-640aba6b4e46"),
                                    readJson("updateProgressFailed/request/negative_case_project_not_found.json"),
                                    readJson("updateProgressFailed/response/negative_case_project_not_found.json"),
                                    HttpStatus.NOT_FOUND),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    UUID.randomUUID(),
                                    readJson("updateProgressFailed/request/negative_case_milestone_not_found.json"),
                                    readJson("updateProgressFailed/response/negative_case_milestone_not_found.json"),
                                    HttpStatus.NOT_FOUND),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    UUID.randomUUID(),
                                    readJson("updateProgressFailed/request/negative_case_progress_less_zero.json"),
                                    readJson("updateProgressFailed/response/negative_case_progress_less_zero.json"),
                                    HttpStatus.BAD_REQUEST),
                Arguments.arguments(UUID.fromString("ffd2f49a-5e5c-4df2-acfe-94d47c05ab5f"),
                                    UUID.randomUUID(),
                                    readJson("updateProgressFailed/request/negative_case_progress_more_100.json"),
                                    readJson("updateProgressFailed/response/negative_case_progress_more_100.json"),
                                    HttpStatus.BAD_REQUEST)
        );
    }

    private static String readJson(final String path, final Object... args) {
        final String content = readContentFromClassPathResource("/json/MilestoneControllerTest/" + path);
        return String.format(content, args);
    }

    @ParameterizedTest
    @MethodSource("createSuccessArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/milestone/data.sql")
    void createSuccess(final UUID projectId, final String request, final String response) {
        mvc.perform(post(String.format(BASE_URL, projectId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(content().json(response, false));

        assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(MILESTONE)))
                .isEqualTo(3);
    }

    @ParameterizedTest
    @MethodSource("createFailedArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/milestone/data.sql")
    void createFailed(final UUID projectId, final String request, final String response, final HttpStatus status) {
        mvc.perform(post(String.format(BASE_URL, projectId))
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                .andExpect(status().is(status.value()))
                .andExpect(content().json(response, true));

        assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(MILESTONE)))
                .isEqualTo(2);
    }

    @ParameterizedTest
    @MethodSource("getSuccessArgs")
    @SneakyThrows
    @Sql(scripts = {
            "classpath:db/project/milestone/data.sql",
            "classpath:db/project/milestone/add_milestones.sql"
    })
    void getSuccess(final UUID projectId, final UUID milestoneId, final String response) {
        mvc.perform(get(String.format(BASE_URL, projectId) + milestoneId)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(response, true));
    }

    @ParameterizedTest
    @MethodSource("getFailedArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/milestone/data.sql")
    void getFailed(final UUID projectId, final UUID milestoneId, final String response) {
        mvc.perform(get(String.format(BASE_URL, projectId) + milestoneId)
                            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response, true));
    }

    @ParameterizedTest
    @MethodSource("updateSuccessArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/milestone/data.sql")
    void updateSuccess(final UUID projectId, final UUID milestoneId, final String request) {
        final SelectConditionStep<Record> selectMilestoneNotEqualUpdated = dsl.selectFrom(MILESTONE)
                .where(MILESTONE.ID.notEqual(milestoneId));

        final Milestone milestoneBeforeUpdate = transactionalUtil.txRun(() -> milestoneRepository.findByIdWithProject(projectId, milestoneId));
        final List<Milestone> milestonesBeforeUpdate = transactionalUtil.txRun(() -> selectMilestoneNotEqualUpdated
                .fetchStream()
                .map(mapper::map)
                .collect(Collectors.toList()));

        mvc.perform(put(String.format(BASE_URL, projectId) + milestoneId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                .andExpect(status().isOk());

        assertThat(transactionalUtil.txRun(() -> milestoneRepository.findByIdWithProject(projectId, milestoneId)))
                .isNotEqualTo(milestoneBeforeUpdate)
                .satisfies(milestone -> assertThat(milestone.getId()).isEqualTo(milestone.getId()));

        transactionalUtil.txRun(() -> {
            selectMilestoneNotEqualUpdated.fetchStream()
                    .map(mapper::map)
                    .forEach(milestone -> assertThat(milestonesBeforeUpdate).contains(milestone));
        });
    }

    @ParameterizedTest
    @MethodSource("updateFailedArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/milestone/data.sql")
    void updateFailed(final UUID projectId,
                      final UUID milestoneId,
                      final String request,
                      final String response,
                      final HttpStatus status) {
        mvc.perform(put(String.format(BASE_URL, projectId) + milestoneId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                .andExpect(status().is(status.value()))
                .andExpect(content().json(response, true));
    }

    @Test
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/milestone/data.sql")
    void deleteSuccess() {
        final UUID projectId = UUID.fromString("4e7efeef-553f-4996-bc03-1c0925d56946");
        final UUID milestoneId = UUID.fromString("fda0c0f6-b637-4039-a2d6-640aba6b4e46");

        mvc.perform(delete(String.format(BASE_URL, projectId) + milestoneId))
                .andExpect(status().isNoContent());

        assertThat(transactionalUtil.txRun(() -> dsl.fetchExists(MILESTONE, MILESTONE.ID.eq(milestoneId).and(MILESTONE.PROJECT_ID.eq(projectId)))))
                .isFalse();

        assertThat(transactionalUtil.txRun(() -> dsl.fetchCount(MILESTONE)))
                .isOne();
    }

    @ParameterizedTest
    @MethodSource("deleteFailedArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/milestone/data.sql")
    void deleteFailed(final UUID projectId, final UUID milestoneId, final String response) {
        mvc.perform(delete(String.format(BASE_URL, projectId) + milestoneId))
                .andExpect(status().isNotFound())
                .andExpect(content().json(response, true));
    }

    @ParameterizedTest
    @MethodSource("updateProgressSuccessArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/milestone/data.sql")
    void updateProgressSuccess(final UUID projectId, final UUID milestoneId, final String request) {
        mvc.perform(patch(String.format(BASE_URL, projectId) + milestoneId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                .andExpect(status().isNoContent());
    }

    @ParameterizedTest
    @MethodSource("updateProgressFailedArgs")
    @SneakyThrows
    @Sql(scripts = "classpath:db/project/milestone/data.sql")
    void updateProgressFailed(final UUID projectId,
                              final UUID milestoneId,
                              final String request,
                              final String response,
                              final HttpStatus status) {
        mvc.perform(patch(String.format(BASE_URL, projectId) + milestoneId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(request))
                .andExpect(status().is(status.value()))
                .andExpect(content().json(response, true));
    }
}
