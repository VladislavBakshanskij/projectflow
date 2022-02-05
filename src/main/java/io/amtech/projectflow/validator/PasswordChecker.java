package io.amtech.projectflow.validator;

import io.amtech.projectflow.dto.request.token.TokenLoginDto;
import io.amtech.projectflow.model.User;
import io.amtech.projectflow.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
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
            errors.reject(String.valueOf(HttpStatus.BAD_REQUEST.value()), "Пароль или логин не совпадают");
        }
    }
}
