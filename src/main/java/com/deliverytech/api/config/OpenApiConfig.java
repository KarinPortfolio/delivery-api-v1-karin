package com.deliverytech.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.security.SecurityRequirement; 
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;

import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "DeliveryTech API",
        version = "1.0.0",
        description = "Documentação da API para o sistema DeliveryTech.",
        contact = @Contact(
            name = "Delivery Tech",
            email = "contato@deliverytech.com"
        )
    ),
    servers = { @Server(url = "http://localhost:8081", description = "Servidor Local de Desenvolvimento") },
    
    security = { @SecurityRequirement(name = "bearerAuth") }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Token JWT para autenticação. Ex: Bearer <seu_token>"
)
public class OpenApiConfig {
   
}