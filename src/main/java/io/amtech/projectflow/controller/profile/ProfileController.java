package io.amtech.projectflow.controller.profile;

import io.amtech.projectflow.dto.request.profile.ProfileDto;
import io.amtech.projectflow.service.profile.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("profile")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping
    public ProfileDto getProfile(@AuthenticationPrincipal final String token) {
        return profileService.getByAccess(token);
    }
}
