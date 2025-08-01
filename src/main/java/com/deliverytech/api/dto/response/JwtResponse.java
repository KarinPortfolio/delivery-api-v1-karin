package com.deliverytech.api.dto.response;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String refreshToken;
    private Long id;
    private String username;
    private String email;

    private Collection<? extends GrantedAuthority> authorities;

    public JwtResponse() {}

    public JwtResponse(String token, String refreshToken, Long id, String username, Collection<? extends GrantedAuthority> authorities) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.id = id;
        this.username = username;
        this.authorities = authorities;
    }

    // Getters and setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getRefreshToken() { return refreshToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Collection<? extends GrantedAuthority> getAuthorities() { return authorities; }
    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) { this.authorities = authorities; }
}
