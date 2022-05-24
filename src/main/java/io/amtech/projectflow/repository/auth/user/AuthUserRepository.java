package io.amtech.projectflow.repository.auth.user;

import io.amtech.projectflow.model.auth.UserWithEmployee;

public interface AuthUserRepository {
    UserWithEmployee findByUsername(String username);
}
