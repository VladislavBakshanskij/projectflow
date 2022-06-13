package io.amtech.projectflow.dto.response.project.flow;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ProjectFlowResponse {
    private String status;
}
