package io.amtech.projectflow.service.profile;

import io.amtech.projectflow.dto.request.profile.ProfileDto;
import io.amtech.projectflow.model.auth.Token;
import io.amtech.projectflow.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {
    private final TokenService tokenService;

    @Override
    public ProfileDto getByAccess(final String access) {
        final Token token = tokenService.getByAccess(access);
        return new ProfileDto()
                .setUser(new ProfileDto.User()
                                 .setId(token.getUser().getUser().getEmployeeId())
                                 .setUsername(token.getUser().getUser().getLogin()))
                .setEmployee(new ProfileDto.Employee()
                                     .setId(token.getUser().getEmployee().getId())
                                     .setPosition(token.getUser().getEmployee().getUserPosition().name()));
    }
}
