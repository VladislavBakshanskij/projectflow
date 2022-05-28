package io.amtech.projectflow.service.project.comment;

import io.amtech.projectflow.dto.request.project.comment.CommentCreateDto;
import io.amtech.projectflow.dto.response.project.comment.CommentDto;
import io.amtech.projectflow.model.project.ProjectComment;
import io.amtech.projectflow.repository.project.ProjectRepository;
import io.amtech.projectflow.repository.project.comment.ProjectCommentRepository;
import io.amtech.projectflow.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final ProjectRepository projectRepository;
    private final ProjectCommentRepository projectCommentRepository;
    private final UserService userService;

    @Override
    public CommentDto create(final UUID projectId, final CommentCreateDto dto) {
        projectRepository.checkOnExists(projectId);
        final String login = userService.getLoginAuthenticatedUser();
        final ProjectComment commentToSave = new ProjectComment()
                .setLogin(login)
                .setCreateDate(Instant.now())
                .setProjectId(projectId)
                .setMessage(dto.getMessage());
        final ProjectComment savedComment = projectCommentRepository.save(commentToSave);
        return new CommentDto()
                .setId(savedComment.getId())
                .setProjectId(savedComment.getProjectId())
                .setMessage(savedComment.getMessage())
                .setCreateDate(savedComment.getCreateDate())
                .setLogin(savedComment.getLogin());
    }
}
