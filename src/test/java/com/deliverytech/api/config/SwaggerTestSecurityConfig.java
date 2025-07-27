package com.deliverytech.api.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de segurança específica para testes de controllers.
 * 
 * IMPORTANTE: Esta configuração só é ativada quando:
 * 1. Explicitamente importada nos testes com @Import(SwaggerTestSecurityConfig.class)
 * 2. Usa @Primary APENAS no contexto de teste para não afetar a aplicação principal
 * 3. Não conflita com o Swagger em desenvolvimento pois desabilita segurança apenas em testes
 * 
 * Como usar:
 * @WebMvcTest(MeuController.class)
 * @Import(SwaggerTestSecurityConfig.class)
 * class MeuControllerTest { ... }
 */
@TestConfiguration
@Profile("test")
public class SwaggerTestSecurityConfig {
    
    /**
     * SecurityFilterChain customizado para testes que:
     * 1. Permite todas as requisições sem autenticação (apenas em testes)
     * 2. Mantém configurações básicas para compatibilidade com a config principal
     * 3. Usa @Primary apenas no contexto de teste e não afeta produção/desenvolvimento
     * 4. Preserva endpoints do Swagger que já estão liberados na configuração principal
     */
    @Bean("testSecurityFilterChain")
    @Primary
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            // Mantém as mesmas configurações básicas da configuração principal
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Permite acesso a todos os endpoints para testes
            .authorizeHttpRequests(authz -> authz
                // Endpoints já liberados na configuração principal (mantém compatibilidade)
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/test/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**").permitAll()
                .requestMatchers("/api-docs/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                // Libera TODOS os endpoints da API para testes (principal diferença)
                .requestMatchers("/api/v1/usuarios/**").permitAll()
                .requestMatchers("/api/v1/clientes/**").permitAll()
                .requestMatchers("/api/v1/restaurantes/**").permitAll()
                .requestMatchers("/api/v1/produtos/**").permitAll()
                .requestMatchers("/api/v1/pedidos/**").permitAll()
                .requestMatchers("/api/v1/entregas/**").permitAll()
                .requestMatchers("/api/v1/entregadores/**").permitAll()
                .anyRequest().permitAll()
            )
            // Headers básicos para compatibilidade com H2 e outros recursos
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            );
        return http.build();
    }
}
