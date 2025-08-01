package com.deliverytech.api.security.service;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;

public class UserDetailsImpl implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private String email;
    // ...existing code...
    public Long getId() { return id; }
    public String getEmail() { return email; }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return null; }
    @Override
    public String getPassword() { return password; }
    @Override
    public String getUsername() { return username; }
    @Override
    public boolean isAccountNonExpired() { return true; }
    @Override
    public boolean isAccountNonLocked() { return true; }
    @Override
    public boolean isCredentialsNonExpired() { return true; }
    @Override
    public boolean isEnabled() { return true; }
}
