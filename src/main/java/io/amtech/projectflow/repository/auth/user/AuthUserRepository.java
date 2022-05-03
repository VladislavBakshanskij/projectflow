package io.amtech.projectflow.repository.auth.user;

import io.amtech.projectflow.model.auth.User;
import io.amtech.projectflow.model.auth.UserWithEmployee;

public interface AuthUserRepository {
    UserWithEmployee findByLogin(String username);

    User findByUsername(String username);
}
