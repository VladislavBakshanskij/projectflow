package io.amtech.projectflow.repository.project.comment;

import io.amtech.projectflow.model.project.ProjectComment;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.RecordMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

import static io.amtech.projectflow.jooq.tables.ProjectComment.PROJECT_COMMENT;

@Repository
@RequiredArgsConstructor
@Transactional(propagation = Propagation.MANDATORY)
public class ProjectCommentRepositoryImpl implements ProjectCommentRepository {
    public static final RecordMapper<Record, ProjectComment> mapper =
            record -> new ProjectComment()
                    .setId(record.get(PROJECT_COMMENT.ID))
                    .setProjectId(record.get(PROJECT_COMMENT.PROJECT_ID))
                    .setMessage(record.get(PROJECT_COMMENT.MESSAGE))
                    .setCreateDate(record.get(PROJECT_COMMENT.CREATE_DATE).toInstant())
                    .setLogin(record.get(PROJECT_COMMENT.LOGIN));

    private final DSLContext dsl;

    @Override
    public List<ProjectComment> findByProjectId(final UUID projectId) {
        return dsl.selectFrom(PROJECT_COMMENT)
                .where(PROJECT_COMMENT.PROJECT_ID.eq(projectId))
                .orderBy(PROJECT_COMMENT.CREATE_DATE.desc())
                .fetch()
                .map(mapper);
    }

    @Override
    public ProjectComment save(final ProjectComment commentToSave) {
        return dsl.insertInto(PROJECT_COMMENT)
                .set(PROJECT_COMMENT.ID, UUID.randomUUID())
                .set(PROJECT_COMMENT.PROJECT_ID, commentToSave.getProjectId())
                .set(PROJECT_COMMENT.LOGIN, commentToSave.getLogin())
                .set(PROJECT_COMMENT.MESSAGE, commentToSave.getMessage())
                .set(PROJECT_COMMENT.CREATE_DATE, OffsetDateTime.ofInstant(commentToSave.getCreateDate(), ZoneOffset.systemDefault()))
                .returning()
                .fetchOne()
                .map(mapper);
    }
}
