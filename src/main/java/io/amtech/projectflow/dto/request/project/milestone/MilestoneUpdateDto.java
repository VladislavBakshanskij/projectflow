package io.amtech.projectflow.dto.request.project.milestone;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Data
public class MilestoneUpdateDto {
    @NotBlank
    @Length(max = 255)
    private String name;
    @Length(max = 2048)
    private String description;
    @NotNull
    private Instant plannedStartDate;
    @NotNull
    private Instant plannedFinishDate;
    private Instant factStartDate;
    private Instant factFinishDate;
    @Range(min = 0, max = 100)
    private short progressPercent = 0;

}
