package io.amtech.projectflow.service.user;

import io.amtech.projectflow.model.User;
import io.amtech.projectflow.repository.auth.user.AuthUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthUserRepository authUserRepository;

    @Override
    public User findByLogin(final String username) {
        return authUserRepository.findByUsername(username);
    }
}
