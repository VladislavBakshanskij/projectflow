package io.amtech.projectflow.controller.user;

import io.amtech.projectflow.dto.response.user.UserInfoResponse;
import io.amtech.projectflow.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {
    private final UserService userService;

    @GetMapping("info")
    public UserInfoResponse getInfo(@ApiIgnore @AuthenticationPrincipal String token) {
        return userService.getUserInfo(token);
    }
}
