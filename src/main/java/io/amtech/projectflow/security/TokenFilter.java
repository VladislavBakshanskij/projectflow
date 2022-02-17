package io.amtech.projectflow.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.amtech.projectflow.controller.error.ErrorResponse;
import io.amtech.projectflow.error.AuthException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Slf4j
public class TokenFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper mapper;

    public TokenFilter(final AuthenticationManager authenticationManager, final ObjectMapper mapper, final String... allowedUrls) {
        super(request -> Arrays.stream(allowedUrls).noneMatch(allowedUrl -> request.getRequestURI().endsWith(allowedUrl)));
        super.setAuthenticationManager(authenticationManager);
        super.setContinueChainBeforeSuccessfulAuthentication(true);
        this.mapper = mapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authHeader)) {
            try {
                final ErrorResponse errorResponse = new ErrorResponse()
                        .setCode(HttpStatus.UNAUTHORIZED.value())
                        .setMessage("Пользователь не авторизован");
                response.getOutputStream().write(mapper.writeValueAsBytes(errorResponse));
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                return null;
            } catch (Exception e) {
                log.error("Error on processing authorization", e);
                throw new AuthException("Пользователь не авторизован");
            }
        }

        final String token = authHeader.toLowerCase().replace("bearer ", StringUtils.EMPTY);
        Authentication authenticate = super.getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(token, null));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return authenticate;
    }
}
