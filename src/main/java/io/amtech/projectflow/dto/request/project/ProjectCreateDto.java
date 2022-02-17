package io.amtech.projectflow.dto.request.project;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class ProjectCreateDto {
    @NotBlank
    @Length(max = 255)
    private String name;
    @NotNull
    private UUID projectLeadId;
    @NotNull
    private UUID directionId;
    @Length(max = 2048)
    private String description;
}
