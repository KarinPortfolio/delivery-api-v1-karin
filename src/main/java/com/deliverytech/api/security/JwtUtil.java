package com.deliverytech.api.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.deliverytech.api.model.Usuario;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    // Chave secreta para assinar os tokens JWT
    // Em produção, isso deveria vir de uma variável de ambiente
    private final String SECRET_KEY = "JC2GKgrBEXtx43g+LYuHLZ6sHDyX7K5t1mlzgOp8J/vskql8+qBvAIdLqO+WUypIkPmGoN5xxpYOnUD3D0ywLg==";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getSignKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    /**
     * Gera um token JWT com as informações do usuário.
     * Inclui userId, role e, se aplicável, restauranteId nos claims.
     * @param userDetails Detalhes do usuário do Spring Security.
     * @param usuario Entidade Usuario.
     * @return O token JWT assinado.
     */
    public String generateToken(UserDetails userDetails, Usuario usuario) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", usuario.getId());
        claims.put("role", usuario.getRole());

        try {
            if (usuario.getRestaurante() != null) {
                claims.put("restauranteId", usuario.getRestaurante().getId());
            }
        } catch (Exception e) {
            System.out.println("Warning: Could not access restaurant for user " + usuario.getEmail() + ": " + e.getMessage());
        }

        return createToken(claims, userDetails.getUsername());
    }
    
    /**
     * Gera um token JWT apenas com o username como subject.
     * Útil para o fluxo de refresh token, onde não é necessário recarregar
     * todos os dados do usuário.
     * @param username O nome de usuário para o subject do token.
     * @return O token JWT assinado.
     */
    public String generateTokenFromUsername(String username) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        long expiration = 1000 * 60 * 60 * 24; // 24 horas
        return Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignKey())
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }
}