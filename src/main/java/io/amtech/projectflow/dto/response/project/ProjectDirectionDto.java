package io.amtech.projectflow.dto.response.project;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class ProjectDirectionDto {
    private UUID id;
    private String name;
}
