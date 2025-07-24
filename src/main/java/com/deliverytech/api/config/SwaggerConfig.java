package com.deliverytech.api.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;


@Configuration 
@OpenAPIDefinition( 
    info = @Info(
        title = "DeliveryTech API", 
        version = "1.0.0", 
        description = "Documentação da API para o sistema de entregas DeliveryTech. " +
                      "Esta API permite gerenciar clientes, entregadores, pedidos, produtos e restaurantes.", // Descrição da API
        contact = @Contact( 
            name = "Seu Nome/Time de Desenvolvimento",
            email = "contato@deliverytech.com",
            url = "https://www.deliverytech.com" 
        ),
        license = @License( 
            name = "Apache 2.0",
            url = "http://www.apache.org/licenses/LICENSE-2.0.html"
        )
    ),
    servers = { 
        @Server(url = "http://localhost:8081", description = "Servidor Local de Desenvolvimento")
       
    }
)
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP, 
    scheme = "bearer", 
    bearerFormat = "JWT", 
    description = "Token JWT para autenticação. Inclua 'Bearer ' antes do token. Ex: Bearer eyJhbGciOi..."
)
public class SwaggerConfig {

}