package com.deliverytech.api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test") // Não carrega durante os testes
public class SwaggerConfig {
    // Empty: springdoc-openapi works out-of-the-box. Add custom beans if needed.
}

