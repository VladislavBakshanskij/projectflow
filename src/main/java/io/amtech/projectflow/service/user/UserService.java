package io.amtech.projectflow.service.user;

import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    String getLoginAuthenticatedUser();
}
