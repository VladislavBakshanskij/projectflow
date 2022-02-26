package io.amtech.projectflow.model.direction;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class Direction {
    private UUID id;
    private UUID leadId;
    private String name;
}
