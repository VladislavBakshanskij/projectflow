package io.amtech.projectflow.service.token;

import io.amtech.projectflow.dto.request.token.TokenLoginDto;
import io.amtech.projectflow.dto.request.token.TokenRefreshDto;
import io.amtech.projectflow.dto.response.token.LogoutDto;
import io.amtech.projectflow.dto.response.token.TokenDto;
import io.amtech.projectflow.error.AuthException;
import io.amtech.projectflow.model.auth.Token;
import io.amtech.projectflow.model.auth.UserWithEmployee;
import io.amtech.projectflow.repository.auth.user.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UUIDTokenGeneratorImpl implements TokenGenerator {
    private final TokenService tokenService;
    private final AuthUserRepository authUserRepository;

    @Override
    public TokenDto generate(final TokenLoginDto dto) {
        try {
            log.debug("Try to generate info for user :: {}", dto.getUsername());
            final UUID accessToken = UUID.randomUUID();
            final UUID refreshToken = UUID.randomUUID();
            final UserWithEmployee user = authUserRepository.findByUsername(dto.getUsername());
            final Token token = new Token()
                    .setAccess(accessToken.toString())
                    .setRefresh(refreshToken.toString())
                    .setUser(user);
            tokenService.store(token);
            log.debug("Generated token :: {}", token);
            return new TokenDto()
                    .setAccess(accessToken)
                    .setRefresh(refreshToken);
        } catch (Exception e) {
            log.error("Error on generate/store token", e);
            throw new AuthException("Неверное имя пользователя");
        }
    }

    @Override
    public TokenDto refresh(final TokenRefreshDto dto) {
        log.debug("Start refresh token :: {}", dto);
        Token token = tokenService.getByRefresh(dto.getRefreshToken());
        final UUID newAccessToken = UUID.randomUUID();
        final UUID newRefreshToken = UUID.randomUUID();
        log.debug("Start store new tokens into redis completed");
        tokenService.store(new Token()
                                   .setAccess(newAccessToken.toString())
                                   .setRefresh(newRefreshToken.toString())
                                   .setUser(token.getUser()));
        log.debug("Stored new tokens into redis completed");
        log.debug("Start remove old tokens from redis");
        tokenService.remove(token);
        log.debug("Removed old tokens from redis");
        return new TokenDto()
                .setAccess(newAccessToken)
                .setRefresh(newRefreshToken);
    }

    @Override
    public LogoutDto remove(final String token) {
        log.debug("Start remove token :: {}", token);
        tokenService.remove(token);
        return new LogoutDto().setStatus(HttpStatus.OK.name());
    }
}
