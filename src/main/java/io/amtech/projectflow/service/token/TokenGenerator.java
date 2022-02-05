package io.amtech.projectflow.service.token;

import io.amtech.projectflow.dto.response.token.TokenDto;

public interface TokenGenerator {
    TokenDto generate(String username);

    TokenDto refresh(String refreshToken);

    void remove(String token);
}
