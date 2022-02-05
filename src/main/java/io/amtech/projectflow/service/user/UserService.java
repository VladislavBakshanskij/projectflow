package io.amtech.projectflow.service.user;

import io.amtech.projectflow.model.User;

public interface UserService {
    User findByLogin(String username);
}
