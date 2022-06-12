package io.amtech.projectflow.dto.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProjectDeletedDto {
    private String status;
}
