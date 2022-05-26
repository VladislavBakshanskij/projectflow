package io.amtech.projectflow.dto.response.project;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class LeadDto {
    private UUID id;
    private String name;
}

