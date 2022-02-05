package io.amtech.projectflow.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class DirectionWithLeadName {
    private UUID id;
    private UUID leadId;
    private String name;
    private String leadName;
}
