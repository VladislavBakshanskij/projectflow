package io.amtech.projectflow.security;

import io.amtech.projectflow.model.auth.Token;
import io.amtech.projectflow.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;
import org.jooq.tools.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

@RequiredArgsConstructor
public class TokenAuthenticationManager implements AuthenticationManager {
    private final TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String userToken = ObjectUtils.defaultIfNull(authentication.getPrincipal(), StringUtils.EMPTY).toString();
        if (StringUtils.isBlank(userToken)) {
            throw new BadCredentialsException("Пользователь не авторизован");
        }
        Token token = tokenService.getByAccess(userToken);
        return new UsernamePasswordAuthenticationToken(userToken, token,
                Collections.singletonList(new SimpleGrantedAuthority("USER")));
    }
}
