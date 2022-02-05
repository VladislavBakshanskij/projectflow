package io.amtech.projectflow.service.token;

import io.amtech.projectflow.model.Token;

public interface TokenService {
    Token getByAccess(String access);

    Token getByRefresh(String refresh);

    void store(Token token);

    void remove(String access);

    void remove(Token token);
}
