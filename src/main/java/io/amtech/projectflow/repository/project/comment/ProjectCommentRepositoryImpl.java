package io.amtech.projectflow.repository.project.comment;

import io.amtech.projectflow.model.project.ProjectComment;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

import static io.amtech.projectflow.jooq.tables.ProjectComment.PROJECT_COMMENT;

@Repository
@RequiredArgsConstructor
public class ProjectCommentRepositoryImpl implements ProjectCommentRepository {
    private final DSLContext dsl;

    @Override
    public List<ProjectComment> findByProjectId(final UUID projectId) {
        return dsl.selectFrom(PROJECT_COMMENT)
                .where(PROJECT_COMMENT.PROJECT_ID.eq(projectId))
                .orderBy(PROJECT_COMMENT.CREATE_DATE)
                .fetch()
                .map(record -> new ProjectComment()
                        .setId(record.get(PROJECT_COMMENT.ID))
                        .setProjectId(record.get(PROJECT_COMMENT.PROJECT_ID))
                        .setMessage(record.get(PROJECT_COMMENT.MESSAGE))
                        .setCreateDate(record.get(PROJECT_COMMENT.CREATE_DATE).toInstant())
                        .setLogin(record.get(PROJECT_COMMENT.LOGIN)));
    }
}
