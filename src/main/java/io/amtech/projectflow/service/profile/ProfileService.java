package io.amtech.projectflow.service.profile;

import io.amtech.projectflow.dto.request.profile.ProfileDto;

public interface ProfileService {
    ProfileDto getByAccess(String access);
}
