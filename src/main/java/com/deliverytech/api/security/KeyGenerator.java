package com.deliverytech.api.security;

import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Base64;

public class KeyGenerator {

    public static void main(String[] args) {
       
        Key secretKey = Keys.secretKeyFor(io.jsonwebtoken.SignatureAlgorithm.HS512);

        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());

        System.out.println("Generated HS512 Secret Key (Base64 encoded):");
        System.out.println(encodedKey);
    }
}
