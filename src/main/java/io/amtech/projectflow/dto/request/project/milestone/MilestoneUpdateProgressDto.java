package io.amtech.projectflow.dto.request.project.milestone;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class MilestoneUpdateProgressDto {
    @Range(min = 0, max = 100)
    private short progress = 0;
}
