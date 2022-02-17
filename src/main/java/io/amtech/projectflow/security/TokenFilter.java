package io.amtech.projectflow.security;

import io.amtech.projectflow.error.AuthException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

public class TokenFilter extends AbstractAuthenticationProcessingFilter {
    public TokenFilter(final AuthenticationManager authenticationManager, final String... allowedUrls) {
        super(request -> Arrays.stream(allowedUrls).noneMatch(allowedUrl -> request.getRequestURI().endsWith(allowedUrl)));
        super.setAuthenticationManager(authenticationManager);
        super.setContinueChainBeforeSuccessfulAuthentication(true);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(authHeader)) {
            throw new AuthException("Пользователь не авторизован");
        }

        final String token = authHeader.toLowerCase().replace("bearer ", StringUtils.EMPTY);
        Authentication authenticate = super.getAuthenticationManager()
                .authenticate(new UsernamePasswordAuthenticationToken(token, null));
        SecurityContextHolder.getContext().setAuthentication(authenticate);
        return authenticate;
    }
}
