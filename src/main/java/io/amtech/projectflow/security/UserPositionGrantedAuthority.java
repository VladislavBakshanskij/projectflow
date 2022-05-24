package io.amtech.projectflow.security;

import io.amtech.projectflow.model.employee.UserPosition;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class UserPositionGrantedAuthority implements GrantedAuthority {
    private final UserPosition userPosition;

    @Override
    public String getAuthority() {
        return userPosition.name();
    }
}
