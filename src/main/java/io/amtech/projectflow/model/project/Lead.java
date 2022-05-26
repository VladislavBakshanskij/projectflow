package io.amtech.projectflow.model.project;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class Lead {
    private UUID id;
    private String name;
}
