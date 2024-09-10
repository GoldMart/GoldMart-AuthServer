package com.goldmarket.common.auth;

import com.goldmarket.member.dto.CustomUserInfo;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final CustomUserInfo info;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() { return info.getPassword(); }

    @Override
    public String getUsername() {
        return info.getName();
    }
}
