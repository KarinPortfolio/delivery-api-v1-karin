package com.deliverytech.api.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @org.springframework.lang.NonNull HttpServletRequest request,
            @org.springframework.lang.NonNull HttpServletResponse response,
            @org.springframework.lang.NonNull FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ") && authHeader.length() > 7 && !authHeader.substring(7).isBlank()) {
                String jwt = authHeader.substring(7);
                try {
                    String username = jwtUtil.extractUsername(jwt);
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // Always load userDetails for test compatibility
                        org.springframework.security.core.userdetails.UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        // Extract role from JWT claim
                        String role = null;
                        try {
                            Object roleClaim = jwtUtil.extractClaim(jwt, claims -> claims.get("role"));
                            if (roleClaim != null) {
                                role = roleClaim.toString();
                            }
                        } catch (Exception e) {
                            System.err.println("Erro ao extrair role do JWT: " + e.getMessage());
                        }
                        if (jwtUtil.isTokenValid(jwt, userDetails)) {
                            java.util.List<org.springframework.security.core.GrantedAuthority> authorities;
if (role != null) {
    authorities = java.util.Collections.singletonList(
        new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role)
    );
} else {
    
    authorities = new java.util.ArrayList<>(userDetails.getAuthorities());
}
                            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                                    userDetails, null, authorities);
                            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authToken);
                        }
                    }
                } catch (ExpiredJwtException e) {
                    System.err.println("Token expirado: " + e.getMessage());
                } catch (io.jsonwebtoken.MalformedJwtException | SignatureException | IllegalArgumentException e) {
                    System.err.println("Erro ao processar JWT: " + e.getMessage());
                }
            }
        } catch (RuntimeException e) {
            System.err.println("Exceção inesperada no filtro JWT (externo): " + e.getMessage());
        } finally {
            try {
                filterChain.doFilter(request, response);
            } catch (IOException | ServletException ignored) {
                // Garante que não propague exceção do filterChain
            }
        }
    }
}