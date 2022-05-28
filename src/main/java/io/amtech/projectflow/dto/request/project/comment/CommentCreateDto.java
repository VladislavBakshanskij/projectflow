package io.amtech.projectflow.dto.request.project.comment;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
public class CommentCreateDto {
    @NotBlank
    @Length(max = 5000)
    private String message;
}
