package com.deliverytech.api.security;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;

public class KeyGenerator {

    public static void main(String[] args) {
       
        // Generate a random 64-byte key for HS512 (512 bits)
        byte[] keyBytes = new byte[64];
        new java.security.SecureRandom().nextBytes(keyBytes);
        Key secretKey = Keys.hmacShaKeyFor(keyBytes);

        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println("Generated HS512 Secret Key (Base64 encoded):");
        System.out.println(encodedKey);
    }
}
