package io.amtech.projectflow.dto.request.token;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TokenLoginDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
