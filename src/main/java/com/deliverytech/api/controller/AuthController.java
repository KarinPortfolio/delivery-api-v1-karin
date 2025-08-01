package com.deliverytech.api.controller;

import com.deliverytech.api.dto.request.LoginRequest;
import com.deliverytech.api.dto.request.RegisterRequest;
import com.deliverytech.api.dto.request.TokenRefreshRequest;
import com.deliverytech.api.dto.response.ErrorResponse;
import com.deliverytech.api.dto.response.JwtResponse;
import com.deliverytech.api.dto.response.RegisterResponse;
import com.deliverytech.api.dto.response.TokenRefreshResponse;
import com.deliverytech.api.model.RefreshToken;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.repository.RestauranteRepository;
import com.deliverytech.api.repository.UsuarioRepository;
import com.deliverytech.api.security.JwtUtil;
import com.deliverytech.api.security.service.RefreshTokenService;
import com.deliverytech.api.security.service.UserDetailsImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;
import java.util.List;


@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Autenticação", description = "Endpoints para login e registro de usuários")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UsuarioRepository usuarioRepository;
    private final RestauranteRepository restauranteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RefreshTokenService refreshTokenService;

    // Construtor para injeção de dependências
    public AuthController(UsuarioRepository usuarioRepository,
                          RestauranteRepository repository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil,
                          RefreshTokenService refreshTokenService) {
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.refreshTokenService = refreshTokenService;
    }

    @Operation(summary = "Realizar login",
            description = "Autentica um usuário e retorna um Access Token e um Refresh Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas ou usuário inativo",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            log.info("=== LOGIN ===");
            log.info("Email: {}", loginRequest.getEmail());

            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());
            if (usuarioOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "Credenciais inválidas"));
            }
            Usuario usuario = usuarioOpt.get();
            if (usuario.getAtivo() == null || !usuario.getAtivo()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Forbidden", "Usuário inativo"));
            }
            if (!passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "Credenciais inválidas"));
            }

            // Gera o Access Token de curta duração
            String jwt = jwtUtil.generateToken(usuario, usuario);
            // Gera ou encontra o Refresh Token de longa duração
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(usuario.getId());

            log.info("Login bem-sucedido para o email: {}", loginRequest.getEmail());

            return ResponseEntity.ok(new JwtResponse(
                    jwt,
                    refreshToken.getToken(),
                    usuario.getId(),
                    usuario.getEmail(),
                    usuario.getRole() != null ? java.util.Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority(usuario.getRole().name())) : java.util.Collections.emptyList()
            ));
        } catch (Exception e) {
            log.error("Erro durante a autenticação para o email: {}", loginRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "Credenciais inválidas"));
        }
    }

    @Operation(summary = "Renovar token de acesso",
            description = "Utiliza um Refresh Token para obter um novo Access Token")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Token de acesso renovado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "401", description = "Refresh Token inválido ou expirado",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        log.info("Tentativa de refresh de token com: {}", requestRefreshToken);

        try {
            // Busca o Refresh Token no banco de dados
            return refreshTokenService.findByToken(requestRefreshToken)
                    .map(refreshTokenService::verifyExpiration)
                    .map(token -> {
                        // Se o token for válido, gera um novo Access Token
                        String newJwt = jwtUtil.generateTokenFromUsername(token.getUser().getEmail());
                        log.info("Novo token de acesso gerado com sucesso.");
                        return ResponseEntity.ok(new TokenRefreshResponse(newJwt, token.getToken()));
                    })
                    .orElseThrow(() -> new RuntimeException("Refresh token inválido ou não encontrado."));
        } catch (Exception e) {
            log.error("Falha ao renovar o token: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", e.getMessage()));
        }
    }

    @Operation(summary = "Registrar novo usuário",
            description = "Cria uma nova conta de usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Email já existe ou dados inválidos",
                    content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest registerRequest) {
        log.info("Tentativa de registro para o email: {}", registerRequest.getEmail());

        if (usuarioRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            log.warn("Tentativa de registro com email já cadastrado: {}", registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Email já cadastrado"));
        }

        Restaurante restaurante = null;
        if (registerRequest.getRole() == Role.RESTAURANTE) {
            if (registerRequest.getRestauranteId() == null) {
                log.warn("Tentativa de registro de RESTAURANTE sem restauranteId: {}", registerRequest.getEmail());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Usuário RESTAURANTE deve ter um restaurante"));
            }
            Optional<Restaurante> restauranteOpt = restauranteRepository.findById(registerRequest.getRestauranteId());
            if (!restauranteOpt.isPresent()) {
                log.warn("Tentativa de registro de RESTAURANTE com restauranteId não encontrado: {}", registerRequest.getRestauranteId());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Restaurante não encontrado"));
            }
            restaurante = restauranteOpt.get();
        } else if (registerRequest.getRestauranteId() != null) {
            Optional<Restaurante> restauranteOpt = restauranteRepository.findById(registerRequest.getRestauranteId());
            restauranteOpt.ifPresent(value -> log.warn("RestauranteId fornecido para role não-RESTAURANTE mas não encontrado: {}", registerRequest.getRestauranteId()));
            restaurante = restauranteOpt.orElse(null);
        }

        Usuario usuario = new Usuario();
        usuario.setNome(registerRequest.getNome());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setSenha(passwordEncoder.encode(registerRequest.getSenha()));
        usuario.setRole(registerRequest.getRole());
        usuario.setRestaurante(restaurante);
        usuario.setAtivo(true);

        try {
            Usuario usuarioSalvo = usuarioRepository.save(usuario);
            log.info("Usuário registrado com sucesso: {}", usuarioSalvo.getEmail());
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new RegisterResponse(usuarioSalvo.getId(), usuarioSalvo.getNome(), usuarioSalvo.getEmail(), usuarioSalvo.getRole()));
        } catch (DataIntegrityViolationException e) {
            log.error("Erro de integridade de dados ao registrar usuário: {}", registerRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Erro de integridade de dados"));
        } catch (Exception e) {
            log.error("Erro interno do servidor ao registrar usuário: {}", registerRequest.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", "Ocorreu um erro inesperado. Tente novamente mais tarde."));
        }
    }
}
