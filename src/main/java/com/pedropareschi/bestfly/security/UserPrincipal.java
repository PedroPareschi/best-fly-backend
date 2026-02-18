package com.pedropareschi.bestfly.security;

import com.pedropareschi.bestfly.entity.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class UserPrincipal implements UserDetails {
    @Getter
    private final Long id;
    private final String email;
    private final String password;
    private final List<GrantedAuthority> authorities;

    public UserPrincipal(Long id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    public static UserPrincipal fromUser(User user) {
        return new UserPrincipal(user.getId(), user.getEmail(), user.getPassword());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
