package com.deliverytech.api.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.api.dto.request.LoginRequest;
import com.deliverytech.api.dto.request.RegisterRequest;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.repository.RestauranteRepository;
import com.deliverytech.api.repository.UsuarioRepository;
import com.deliverytech.api.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
@Tag(name = "Autenticação", description = "Endpoints para login e registro de usuários")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final RestauranteRepository restauranteRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository, 
                         RestauranteRepository restauranteRepository,
                         PasswordEncoder passwordEncoder,
                         AuthenticationManager authenticationManager,
                         JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Realizar login", 
               description = "Autentica um usuário e retorna um token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
                    content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            // Buscar usuário
            Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());
            if (!usuarioOpt.isPresent()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Credenciais inválidas\"}");
            }

            Usuario usuario = usuarioOpt.get();

            // Verificar se o usuário está ativo
            if (!usuario.getAtivo()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("{\"status\": 403, \"error\": \"Forbidden\", \"message\": \"Usuário inativo\"}");
            }

            // Autenticar
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getSenha())
            );

            // Gerar token
            String token = jwtUtil.generateToken(usuario, usuario);

            return ResponseEntity.ok("{\"token\": \"" + token + "\"}");

        } catch (org.springframework.security.core.AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("{\"status\": 401, \"error\": \"Unauthorized\", \"message\": \"Credenciais inválidas\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Erro interno do servidor\"}");
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
        try {
            // Verificar se email já existe
            if (usuarioRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Email já cadastrado\"}");
            }

            // Verificar se é um usuário ADMIN sem restaurante
            if (registerRequest.getRole() == Role.ADMIN && registerRequest.getRestauranteId() != null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Usuário ADMIN não pode ter restaurante\"}");
            }

            // Verificar se é um usuário ADMIN com restaurante válido
            Restaurante restaurante = null;
            if (registerRequest.getRole() == Role.ADMIN) {
                if (registerRequest.getRestauranteId() == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Usuário ADMIN deve ter um restaurante\"}");
                }
                
                Optional<Restaurante> restauranteOpt = restauranteRepository.findById(registerRequest.getRestauranteId());
                if (!restauranteOpt.isPresent()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Restaurante não encontrado\"}");
                }
                restaurante = restauranteOpt.get();
            }

            // Criar usuário
            Usuario usuario = new Usuario();
            usuario.setNome(registerRequest.getNome());
            usuario.setEmail(registerRequest.getEmail());
            usuario.setSenha(passwordEncoder.encode(registerRequest.getSenha()));
            usuario.setRole(registerRequest.getRole());
            usuario.setRestaurante(restaurante);
            usuario.setAtivo(true);

            Usuario usuarioSalvo = usuarioRepository.save(usuario);

            return ResponseEntity.status(HttpStatus.CREATED)
                .body("{\"id\": " + usuarioSalvo.getId() + ", \"nome\": \"" + usuarioSalvo.getNome() + "\", \"email\": \"" + usuarioSalvo.getEmail() + "\", \"role\": \"" + usuarioSalvo.getRole() + "\"}");

        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("{\"status\": 400, \"error\": \"Bad Request\", \"message\": \"Dados inválidos ou já existentes\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("{\"status\": 500, \"error\": \"Internal Server Error\", \"message\": \"Erro interno do servidor\"}");
        }
    }
}
