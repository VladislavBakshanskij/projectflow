package io.amtech.projectflow.service.project.comment;

import io.amtech.projectflow.dto.request.project.comment.CommentCreateDto;
import io.amtech.projectflow.dto.response.project.comment.CommentDto;

import java.util.UUID;

public interface CommentService {
    CommentDto create(UUID projectId, CommentCreateDto dto);
}
