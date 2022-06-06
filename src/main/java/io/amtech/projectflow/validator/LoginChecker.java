package io.amtech.projectflow.validator;

import io.amtech.projectflow.dto.request.token.TokenLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginChecker implements Validator {
    private final AuthenticationManager authenticationManager;

    @Override
    public boolean supports(final Class<?> clazz) {
        return TokenLoginDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(final Object target, final Errors errors) {
        try {
            final TokenLoginDto loginDto = (TokenLoginDto) target;
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        } catch (AuthenticationException e) {
            log.error("Error on try auth user :: {}", target, e);
            errors.reject("username", e.getMessage());
            errors.reject("password", e.getMessage());
        }
    }
}
