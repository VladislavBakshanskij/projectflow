package io.amtech.projectflow.dto.response.token;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class LogoutDto {
    private String status;
}
