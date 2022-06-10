package io.amtech.projectflow.service.user;

import io.amtech.projectflow.dto.response.user.UserInfoResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    String getLoginAuthenticatedUser();

    UserInfoResponse getUserInfo(String token);
}
