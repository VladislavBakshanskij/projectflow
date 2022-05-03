package io.amtech.projectflow.service.token;

import io.amtech.projectflow.dto.request.token.TokenLoginDto;
import io.amtech.projectflow.dto.request.token.TokenRefreshDto;
import io.amtech.projectflow.dto.response.token.TokenDto;

public interface TokenGenerator {
    TokenDto generate(TokenLoginDto dto);

    TokenDto refresh(TokenRefreshDto dto);

    void remove(String token);
}
