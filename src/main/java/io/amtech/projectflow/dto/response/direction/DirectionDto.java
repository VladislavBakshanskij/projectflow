package io.amtech.projectflow.dto.response.direction;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class DirectionDto {
    private UUID id;
    private UUID leadId;
    private String name;
    private String leadName;
}
