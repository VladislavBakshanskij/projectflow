package io.amtech.projectflow.dto.response.project.milestone;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class MilestoneUpdateResponseDto {
    private String status;
}
