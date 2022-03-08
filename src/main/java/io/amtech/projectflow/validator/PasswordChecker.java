package io.amtech.projectflow.validator;

import io.amtech.projectflow.dto.request.token.TokenLoginDto;
import io.amtech.projectflow.model.auth.User;
import io.amtech.projectflow.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@Slf4j
@RequiredArgsConstructor
public class PasswordChecker implements Validator {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean supports(Class<?> clazz) {
        return TokenLoginDto.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        final TokenLoginDto loginDto = (TokenLoginDto) target;
        final User user = userService.findByLogin(loginDto.getUsername());
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            log.warn("Username or password is incorrect :: {} -> {}", loginDto, user);
            errors.reject(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Пароль или логин не совпадают");
        }
    }
}