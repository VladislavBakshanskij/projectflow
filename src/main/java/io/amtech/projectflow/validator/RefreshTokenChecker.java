package io.amtech.projectflow.validator;

import io.amtech.projectflow.dto.request.token.TokenRefreshDto;
import io.amtech.projectflow.model.Token;
import io.amtech.projectflow.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RefreshTokenChecker implements Validator {
    private final TokenService tokenService;

    @Override
    public boolean supports(Class<?> clazz) {
        return TokenRefreshDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final TokenRefreshDto tokenRefreshDto = (TokenRefreshDto) target;
        final Token refreshToken = tokenService.getByRefresh(tokenRefreshDto.getRefreshToken());
        if (Objects.isNull(refreshToken)) {
            errors.reject(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Неверный токен");
        }
    }
}
