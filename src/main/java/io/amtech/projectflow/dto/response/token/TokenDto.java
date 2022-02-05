package io.amtech.projectflow.dto.response.token;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.UUID;

@Data
@Accessors(chain = true)
public class TokenDto {
    private UUID access;
    private UUID refresh;
}
