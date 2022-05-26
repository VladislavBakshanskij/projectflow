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

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class TokenAuthenticationManager implements AuthenticationManager {
    private final TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final String userToken = ObjectUtils.defaultIfNull(authentication.getPrincipal(), StringUtils.EMPTY).toString();
        if (StringUtils.isBlank(userToken)) {
            throw new BadCredentialsException("Пользователь не авторизован");
        }
        final Token token = tokenService.getByAccess(userToken);
        return Optional.ofNullable(token)
                .map(t -> createAuth(userToken, token))
                .orElseThrow(() -> new BadCredentialsException("Не удалось авторизовать пользователя"));
    }

    private UsernamePasswordAuthenticationToken createAuth(String userToken, Token token) {
        List<UserPositionGrantedAuthority> authorities = List.of(new UserPositionGrantedAuthority(token.getUser().getEmployee().getUserPosition()));
        return new UsernamePasswordAuthenticationToken(userToken, token, authorities);
    }
}
