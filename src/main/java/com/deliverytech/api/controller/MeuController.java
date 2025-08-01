package com.deliverytech.api.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeuController {

    // Este endpoint exige autenticação
    @Operation(security = { @SecurityRequirement(name = "bearerAuth") })
    @GetMapping("/dados-protegidos")
    public String getDadosProtegidos() {
        return "Dados confidenciais para usuários autenticados.";
    }

    // Este endpoint é público, não precisa de autenticação
    @GetMapping("/dados-publicos")
    public String getDadosPublicos() {
        return "Este é um endpoint público.";
    }
}