package com.deliverytech.api.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import jakarta.annotation.Generated;

@Generated(value = "PasswordEncoderGenerator.class")
public class PasswordEncoderGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "admin123";
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println("Sua senha codificada é: " + encodedPassword);
    }
}
