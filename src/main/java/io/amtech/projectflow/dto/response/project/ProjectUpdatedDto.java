package io.amtech.projectflow.dto.response.project;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProjectUpdatedDto {
    private String status;
}
