package com.deliverytech.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; // Importação adicionada
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
@OpenAPIDefinition(
    info = @Info(title = "Minha API", version = "v1"),
    security = @SecurityRequirement(name = "bearerAuth") // Anotação adicionada
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    bearerFormat = "JWT",
    scheme = "bearer"
)
public class SwaggerConfig {
    // A classe continua vazia.
}