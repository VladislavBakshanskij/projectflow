package io.amtech.projectflow.dto.request.direction;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Data
public class DirectionUpdateDto {
    @NotBlank
    private String name;
    @NotNull
    private UUID leadId;
}
