package io.amtech.projectflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.amtech.projectflow.controller.error.ErrorResponse;
import io.amtech.projectflow.error.AuthException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenAuthenticationFailureHandler implements AuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        try {
            log.warn("Error on auth user", exception);
            final int code = HttpStatus.UNAUTHORIZED.value();
            final ErrorResponse errorResponse = new ErrorResponse()
                    .setCode(code)
                    .setMessage(exception.getMessage());
            response.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, CorsConfiguration.ALL);
            response.getOutputStream().write(objectMapper.writeValueAsBytes(errorResponse));
            response.setStatus(code);
        } catch (Exception e) {
            log.error("Error on handle fail auth", e);
            throw new AuthException(e.getMessage());
        }
    }
}
