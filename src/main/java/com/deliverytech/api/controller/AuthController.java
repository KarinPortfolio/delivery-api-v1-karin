package com.deliverytech.api.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.api.dto.request.LoginRequest;
import com.deliverytech.api.dto.request.RegisterRequest;
import com.deliverytech.api.dto.response.ErrorResponse; // Importar o novo DTO
import com.deliverytech.api.dto.response.RegisterResponse; // Importar o novo DTO
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.model.Role;
import com.deliverytech.api.model.Usuario;
import com.deliverytech.api.repository.RestauranteRepository;
import com.deliverytech.api.repository.UsuarioRepository;
import com.deliverytech.api.security.JwtUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "*", maxAge = 3600) // @CrossOrigin já lida com OPTIONS
@Tag(name = "Autenticação", description = "Endpoints para login e registro de usuários")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final UsuarioRepository usuarioRepository;
    private final RestauranteRepository restauranteRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UsuarioRepository usuarioRepository, 
                          RestauranteRepository restauranteRepository,
                          PasswordEncoder passwordEncoder,
                          JwtUtil jwtUtil) {
        this.usuarioRepository = usuarioRepository;
        this.restauranteRepository = restauranteRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // O método handleOptions() é redundante com @CrossOrigin(origins = "*", maxAge = 3600)
    // e pode ser removido. O Spring MVC já lida com requisições OPTIONS automaticamente.
    /*
    @RequestMapping(value = "/**", method = RequestMethod.OPTIONS)
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build();
    }
    */

    @Operation(summary = "Realizar login", 
               description = "Autentica um usuário e retorna um token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Login realizado com sucesso",
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Dados inválidos",
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "403", description = "Usuário inativo",
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                     content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        log.info("=== LOGIN ===");
        log.info("Email: {}", loginRequest.getEmail());
        
        // A validação de senha nula/vazia deve ser feita via @NotBlank no LoginRequest DTO
        // e tratada por um @ControllerAdvice.
        // Se você não tem um @ControllerAdvice, a validação @Valid já dispara MethodArgumentNotValidException.

        // Buscar usuário
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(loginRequest.getEmail());
        if (!usuarioOpt.isPresent()) {
            log.warn("Tentativa de login com email inexistente: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "Credenciais inválidas"));
        }

        Usuario usuario = usuarioOpt.get();
        
        // Verificar se usuário está ativo
        if (!usuario.getAtivo()) {
            log.warn("Tentativa de login de usuário inativo: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse(HttpStatus.FORBIDDEN.value(), "Forbidden", "Usuário inativo"));
        }
        
        // Verificar senha
        if (!passwordEncoder.matches(loginRequest.getSenha(), usuario.getSenha())) {
            log.warn("Tentativa de login com senha inválida para o email: {}", loginRequest.getEmail());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized", "Credenciais inválidas"));
        }

        // Gerar token JWT
        String token = jwtUtil.generateToken(usuario, usuario);
        log.info("Login bem-sucedido para o email: {}", loginRequest.getEmail());
        return ResponseEntity.ok("{\"token\": \"" + token + "\"}");
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
        
        // Verificar se email já existe
        if (usuarioRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            log.warn("Tentativa de registro com email já cadastrado: {}", registerRequest.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Email já cadastrado"));
        }

        // Verificar se é um usuário RESTAURANTE que precisa de restauranteId
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
            // Se não é RESTAURANTE mas tem restauranteId, buscar o restaurante (para ADMIN que pode opcionalmente ter)
            Optional<Restaurante> restauranteOpt = restauranteRepository.findById(registerRequest.getRestauranteId());
            if (restauranteOpt.isPresent()) {
                restaurante = restauranteOpt.get();
            } else {
                log.warn("RestauranteId fornecido para role não-RESTAURANTE mas não encontrado: {}", registerRequest.getRestauranteId());
                // Opcional: Você pode retornar um erro 400 aqui se quiser ser mais rigoroso
                // return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                //     .body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request", "Restaurante especificado não encontrado"));
            }
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
        log.info("Usuário registrado com sucesso: {}", usuarioSalvo.getEmail());
        
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(new RegisterResponse(usuarioSalvo.getId(), usuarioSalvo.getNome(), usuarioSalvo.getEmail(), usuarioSalvo.getRole()));
    }
}