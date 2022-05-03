package io.amtech.projectflow.service.token;

import io.amtech.projectflow.dto.request.token.TokenLoginDto;
import io.amtech.projectflow.dto.request.token.TokenRefreshDto;
import io.amtech.projectflow.dto.response.token.TokenDto;
import io.amtech.projectflow.error.AuthException;
import io.amtech.projectflow.model.auth.Token;
import io.amtech.projectflow.model.auth.UserWithEmployee;
import io.amtech.projectflow.repository.auth.user.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UUIDTokenGeneratorImpl implements TokenGenerator {
    private final TokenService tokenService;
    private final AuthUserRepository authUserRepository;

    @Override
    public TokenDto generate(final TokenLoginDto dto) {
        try {
            final UUID accessToken = UUID.randomUUID();
            final UUID refreshToken = UUID.randomUUID();
            final UserWithEmployee user = authUserRepository.findByLogin(dto.getUsername());
            tokenService.store(new Token()
                                       .setAccess(accessToken.toString())
                                       .setRefresh(refreshToken.toString())
                                       .setUser(user));
            return new TokenDto()
                    .setAccess(accessToken)
                    .setRefresh(refreshToken);
        } catch (Exception e) {
            throw new AuthException("Неверное имя пользователя");
        }
    }

    @Override
    public TokenDto refresh(final TokenRefreshDto dto) {
        Token token = tokenService.getByRefresh(dto.getRefreshToken());
        final UUID newAccessToken = UUID.randomUUID();
        final UUID newRefreshToken = UUID.randomUUID();
        tokenService.store(new Token()
                                   .setAccess(newAccessToken.toString())
                                   .setRefresh(newRefreshToken.toString())
                                   .setUser(token.getUser()));
        tokenService.remove(token);
        return new TokenDto()
                .setAccess(newAccessToken)
                .setRefresh(newRefreshToken);
    }

    @Override
    public void remove(final String token) {
        tokenService.remove(token);
    }
}
