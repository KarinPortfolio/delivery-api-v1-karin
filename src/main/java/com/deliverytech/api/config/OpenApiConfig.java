package com.deliverytech.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import jakarta.annotation.Generated;
import java.util.List;

@Configuration
public class OpenApiConfig {
    @Generated (value = "OpenApiConfig", date = "2023-10-01T12:00:00Z")
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Delivery API")
                        .version("1.0.0")
                        .description("API completa para sistema de delivery com validações e exemplos")
                        .contact(new Contact()
                                .name("Equipe Delivery")
                                .email("suporte@delivery.com")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8081")
                                .description("Servidor de Desenvolvimento")
                ));
    }
}