package io.amtech.projectflow.service.user;

import io.amtech.projectflow.model.auth.User;

public interface UserService {
    User findByLogin(String username);
}
