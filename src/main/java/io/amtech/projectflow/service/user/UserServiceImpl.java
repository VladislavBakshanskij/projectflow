package io.amtech.projectflow.service.user;

import io.amtech.projectflow.error.AuthException;
import io.amtech.projectflow.error.DataNotFoundException;
import io.amtech.projectflow.model.auth.Token;
import io.amtech.projectflow.model.auth.User;
import io.amtech.projectflow.model.auth.UserWithEmployee;
import io.amtech.projectflow.repository.auth.user.AuthUserRepository;
import io.amtech.projectflow.security.ProjectFlowUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthUserRepository authUserRepository;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        try {
            final UserWithEmployee user = authUserRepository.findByUsername(username);
            return new ProjectFlowUserDetails(user);
        } catch (DataNotFoundException e) {
            log.error("Error on load info for user {}", username, e);
            throw new UsernameNotFoundException(e.getMessage(), e);
        }
    }

    @Override
    public String getLoginAuthenticatedUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getCredentials)
                .map(Token.class::cast)
                .map(Token::getUser)
                .map(UserWithEmployee::getUser)
                .map(User::getLogin)
                .orElseThrow(() -> new AuthException("Пользоваеть не авторизован"));
    }
}
