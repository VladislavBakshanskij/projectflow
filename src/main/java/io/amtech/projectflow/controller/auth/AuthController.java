package io.amtech.projectflow.controller.auth;

import io.amtech.projectflow.dto.request.token.TokenLoginDto;
import io.amtech.projectflow.dto.request.token.TokenRefreshDto;
import io.amtech.projectflow.dto.response.token.TokenDto;
import io.amtech.projectflow.service.token.TokenGenerator;
import io.amtech.projectflow.validator.PasswordChecker;
import io.amtech.projectflow.validator.RefreshTokenChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final TokenGenerator tokenGenerator;
    private final PasswordChecker passwordChecker;
    private final RefreshTokenChecker refreshTokenChecker;

    @InitBinder("tokenLoginDto")
    public void bindValidationPassword(final WebDataBinder binder) {
        binder.addValidators(passwordChecker);
    }

    @InitBinder("tokenRefreshDto")
    public void bindValidationRefreshToken(final WebDataBinder binder) {
        binder.addValidators(refreshTokenChecker);
    }

    @PostMapping("login")
    public TokenDto login(@Valid @RequestBody TokenLoginDto tokenLoginDto) {
        return tokenGenerator.generate(tokenLoginDto.getUsername());
    }

    @PostMapping("refresh")
    public TokenDto refresh(@Valid @RequestBody TokenRefreshDto tokenRefreshDto) {
        return tokenGenerator.refresh(tokenRefreshDto.getRefreshToken());
    }

    @PostMapping("logout")
    public void logout(@NotBlank @RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        tokenGenerator.remove(token);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
