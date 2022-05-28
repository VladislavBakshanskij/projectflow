package io.amtech.projectflow.controller.project.comment;

import io.amtech.projectflow.dto.request.project.comment.CommentCreateDto;
import io.amtech.projectflow.dto.response.project.comment.CommentDto;
import io.amtech.projectflow.service.project.comment.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("projects/{projectId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto create(@PathVariable UUID projectId,
                             @RequestBody @Valid CommentCreateDto dto) {
        return commentService.create(projectId, dto);
    }
}
