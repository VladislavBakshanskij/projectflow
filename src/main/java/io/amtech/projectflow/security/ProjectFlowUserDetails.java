package io.amtech.projectflow.security;

import io.amtech.projectflow.model.auth.UserWithEmployee;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
public final class ProjectFlowUserDetails implements UserDetails {
    private final UserWithEmployee user;

    @Override
    public Collection<UserPositionGrantedAuthority> getAuthorities() {
        return List.of(new UserPositionGrantedAuthority(user.getEmployee().getUserPosition()));
    }

    @Override
    public String getPassword() {
        return user.getUser().getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUser().getLogin();
    }

    @Override
    public boolean isAccountNonExpired() {
        return !user.getUser().isLocked() && !user.getEmployee().isFired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getUser().isLocked() && !user.getEmployee().isFired();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !user.getUser().isLocked() && !user.getEmployee().isFired();
    }

    @Override
    public boolean isEnabled() {
        return !user.getUser().isLocked() && !user.getEmployee().isFired();
    }
}
