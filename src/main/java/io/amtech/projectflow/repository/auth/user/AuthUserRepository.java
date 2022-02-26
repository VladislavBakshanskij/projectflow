package io.amtech.projectflow.repository.auth.user;

import io.amtech.projectflow.model.auth.User;
import io.amtech.projectflow.model.auth.UserWithEmployee;

import java.util.UUID;

public interface AuthUserRepository {
    User findByEmployeeId(UUID id);

    UserWithEmployee findWithEmployeeByEmployeeId(UUID id);

    UserWithEmployee findByLogin(String username);

    User findByUsername(String username);
}
