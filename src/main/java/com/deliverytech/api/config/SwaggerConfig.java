package com.deliverytech.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test") // Não carrega durante os testes
public class SwaggerConfig {
    // Configuração automática do SpringDoc
    // Não precisa de beans customizados para funcionalidade básica
}
