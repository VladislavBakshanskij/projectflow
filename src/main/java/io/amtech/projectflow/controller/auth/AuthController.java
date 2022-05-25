package io.amtech.projectflow.controller.auth;

import io.amtech.projectflow.dto.request.token.TokenLoginDto;
import io.amtech.projectflow.dto.request.token.TokenRefreshDto;
import io.amtech.projectflow.dto.response.token.TokenDto;
import io.amtech.projectflow.service.token.TokenGenerator;
import io.amtech.projectflow.validator.LoginChecker;
import io.amtech.projectflow.validator.RefreshTokenChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {
    private final TokenGenerator tokenGenerator;
    private final LoginChecker loginChecker;
    private final RefreshTokenChecker refreshTokenChecker;

    @InitBinder("tokenLoginDto")
    public void bindValidationPassword(final WebDataBinder binder) {
        binder.addValidators(loginChecker);
    }

    @InitBinder("tokenRefreshDto")
    public void bindValidationRefreshToken(final WebDataBinder binder) {
        binder.addValidators(refreshTokenChecker);
    }

    @PostMapping("login")
    public TokenDto login(@Valid @RequestBody TokenLoginDto tokenLoginDto) {
        return tokenGenerator.generate(tokenLoginDto);
    }

    @PostMapping("refresh")
    public TokenDto refresh(@Valid @RequestBody TokenRefreshDto tokenRefreshDto) {
        return tokenGenerator.refresh(tokenRefreshDto);
    }

    @PostMapping("logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(@AuthenticationPrincipal String token) {
        tokenGenerator.remove(token);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
