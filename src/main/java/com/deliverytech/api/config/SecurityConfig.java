package com.deliverytech.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfigurationSource;
import jakarta.annotation.Generated;
import com.deliverytech.api.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
@Generated (value = "SecurityConfig", date = "2023-10-01T12:00:00Z")
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CorsConfigurationSource corsConfigurationSource;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, 
                         CorsConfigurationSource corsConfigurationSource) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.corsConfigurationSource = corsConfigurationSource;
    }
    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(org.springframework.http.HttpMethod.DELETE, "/api/v1/usuarios/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.PUT, "/api/v1/usuarios/**").hasRole("ADMIN")
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/v1/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/h2-console/**").permitAll()
                .requestMatchers("/api/v1/auth/**").permitAll()
                .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/v1/debug/**").hasRole("ADMIN")
                .requestMatchers("/api/test/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/swagger-resources/**").permitAll()
                .requestMatchers("/api-docs/**", "/v3/api-docs/**", "/v3/api-docs.yaml").permitAll()
                .requestMatchers("/webjars/**").permitAll()
                .requestMatchers("/error").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers("/api/v1/usuarios/**").hasRole("ADMIN")
                // Permite leitura (GET)
                .requestMatchers(HttpMethod.GET, "/api/v1/restaurantes/**").hasAnyRole("ADMIN", "CLIENTE", "RESTAURANTE", "ENTREGADOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/entregas/**").hasAnyRole("ADMIN","CLIENTE", "RESTAURANTE", "ENTREGADOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/produtos/**").hasAnyRole("ADMIN","CLIENTE", "RESTAURANTE", "ENTREGADOR")
                .requestMatchers(HttpMethod.GET, "/api/v1/pedidos/**").hasAnyRole("ADMIN","CLIENTE", "RESTAURANTE", "ENTREGADOR")
                // Permite editar (POST, PUT, DELETE) para endpoints de restaurantes
                .requestMatchers("/api/v1/restaurantes/**").hasAnyRole("ADMIN", "RESTAURANTE", "ENTREGADOR")
                .requestMatchers("/api/v1/entregas/**").hasAnyRole("ADMIN","RESTAURANTE", "ENTREGADOR")
                .requestMatchers("/api/v1/produtos/**").hasAnyRole("ADMIN","RESTAURANTE", "ENTREGADOR")
                .requestMatchers("/api/v1/pedidos/**").hasAnyRole("ADMIN", "RESTAURANTE", "ENTREGADOR")
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .headers(headers -> headers
                .frameOptions(frameOptions -> frameOptions.sameOrigin())
            );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}