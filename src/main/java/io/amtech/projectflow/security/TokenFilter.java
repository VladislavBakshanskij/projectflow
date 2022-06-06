package io.amtech.projectflow.security;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class TokenFilter extends AbstractAuthenticationProcessingFilter {
    private static final String TOKEN_TYPE = "bearer ";

    public TokenFilter(final AuthenticationManager authenticationManager, final AuthenticationFailureHandler authenticationFailureHandler) {
        super("/**");
        super.setAuthenticationManager(authenticationManager);
        super.setContinueChainBeforeSuccessfulAuthentication(true);
        super.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authHeader)) {
            throw new BadCredentialsException("Пользователь не авторизован");
        }

        if (!isBearerTokenType(authHeader)) {
            throw new BadCredentialsException("Неверный заголовок авторизации");
        }

        final String token = removeTokenTypeIgnoreCase(authHeader);
        final Authentication authenticate = super.getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(token, null));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return authenticate;
    }

    private String removeTokenTypeIgnoreCase(final String token) {
        return token.toLowerCase().replace(TOKEN_TYPE, StringUtils.EMPTY);
    }

    private boolean isBearerTokenType(final String token) {
        return StringUtils.startsWithIgnoreCase(token, TOKEN_TYPE);
    }
}
