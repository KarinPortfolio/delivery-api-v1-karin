package com.deliverytech.api.controller;

import com.deliverytech.api.dto.request.LoginRequest;
import com.deliverytech.api.dto.request.RegisterRequest;
import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.repository.RestauranteRepository;
import com.deliverytech.api.repository.UsuarioRepository;
import com.deliverytech.api.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus; // Importe para HttpStatus
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException; // Importe para ResponseStatusException

import jakarta.validation.Valid;
import java.util.Optional;

// Importações para OpenAPI/Swagger
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody; // Cuidado para não confundir com Spring @RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import io.swagger.v3.oas.annotations.tags.Tag;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Operações de registro e login de usuários") // Tag para agrupar no Swagger UI
// Não adicione @SecurityRequirement(name = "bearerAuth") aqui, pois login e register são endpoints públicos
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RestauranteRepository restauranteRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Operation(
        summary = "Registra um novo usuário",
        description = "Permite o cadastro de novos usuários (clientes, administradores, entregadores) no sistema. Retorna um token JWT após o registro bem-sucedido.",
        requestBody = @RequestBody(
            description = "Detalhes do novo usuário para registro",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = RegisterRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo de Registro de Cliente",
                    value = "{\"nome\": \"João da Silva\", \"email\": \"joao.silva@example.com\", \"senha\": \"minhasenhaforte\", \"role\": \"CLIENTE\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso, retorna JWT",
                content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1Ni..."))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou email já cadastrado",
                content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Email já cadastrado"))),
            @ApiResponse(responseCode = "404", description = "Restaurante não encontrado (se restauranteId for fornecido e inválido)",
                content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "Restaurante com ID 999 não encontrado.")))
        }
    )
    @PostMapping("/register")
    public ResponseEntity<String> register(@Valid @org.springframework.web.bind.annotation.RequestBody RegisterRequest request) { // <-- AGORA SEMPRE QUALIFICADO
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email já cadastrado");
        }

        Usuario.UsuarioBuilder usuarioBuilder = Usuario.builder()
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .nome(request.getNome())
                .role(request.getRole() != null ? request.getRole() : Role.CLIENTE)
                .ativo(true);

        if (request.getRestauranteId() != null) {
            Optional<Restaurante> restauranteOptional = restauranteRepository.findById(request.getRestauranteId());
            if (restauranteOptional.isPresent()) {
                usuarioBuilder.restaurante(restauranteOptional.get());
            } else {
                return ResponseEntity.badRequest().body("Restaurante com ID " + request.getRestauranteId() + " não encontrado.");
            }
        }

        Usuario usuario = usuarioBuilder.build();
        usuarioRepository.save(usuario);

        // Passando 'usuario' duas vezes para corresponder à assinatura generateToken(UserDetails, Usuario)
        String token = jwtUtil.generateToken(usuario, usuario);
        return ResponseEntity.ok(token);
    }

    @Operation(
        summary = "Autentica um usuário",
        description = "Autentica um usuário com email e senha. Retorna um token JWT se as credenciais forem válidas.",
        requestBody = @RequestBody(
            description = "Credenciais do usuário para login",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = LoginRequest.class),
                examples = @ExampleObject(
                    name = "Exemplo de Login",
                    value = "{\"email\": \"usuario@example.com\", \"senha\": \"senha123\"}"
                )
            )
        ),
        responses = {
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido, retorna JWT",
                content = @Content(mediaType = "text/plain", schema = @Schema(type = "string", example = "eyJhbGciOiJIUzI1Ni..."))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas (email ou senha incorretos)",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Bad credentials\"}"))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado (se o email não existir)",
                content = @Content(mediaType = "application/json", schema = @Schema(example = "{\"status\": 404, \"error\": \"Not Found\", \"message\": \"Usuário não encontrado\"}")))
        }
    )
    @PostMapping("/login")
    public ResponseEntity<String> login(@Valid @org.springframework.web.bind.annotation.RequestBody LoginRequest request) { // <-- AGORA SEMPRE QUALIFICADO
        authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha()));

        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado")); // Lança 404 se não encontrar

        // Passando 'usuario' duas vezes para corresponder à assinatura generateToken(UserDetails, Usuario)
        String token = jwtUtil.generateToken(usuario, usuario);
        return ResponseEntity.ok(token);
    }
}