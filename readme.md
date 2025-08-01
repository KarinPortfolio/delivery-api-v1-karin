readme.md

# delivery-api-karin

API de Delivery - Projeto de Desenvolvimento

# Delivery Tech API

Sistema de delivery desenvolvido com Spring Boot e Java 21.

# Estrutura das pastas

```
.
├── Dockerfile
├── docker-compose.yml
├── mvnw
├── mvnw.cmd
├── pom.xml
├── prometheus.yml
├── readme.md
├── src
│   ├── main
│   │   ├── java
│   │   │   └── com
│   │   │       └── deliverytech
│   │   │           └── api
│   │   │               ├── ApiApplication.java
│   │   │               ├── config
│   │   │               │   ├── CorsConfig.java
│   │   │               │   ├── DataInitializer.java
│   │   │               │   ├── OpenApiConfig.java
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   └── SwaggerConfig.java
│   │   │               ├── controller
│   │   │               │   ├── AdminManagementController.java
│   │   │               │   ├── AuthController.java
│   │   │               │   ├── ClienteController.java
│   │   │               │   ├── DebugController.java
│   │   │               │   ├── EntregaController.java
│   │   │               │   ├── EntregadorController.java
│   │   │               │   ├── PedidoController.java
│   │   │               │   ├── ProdutoController.java
│   │   │               │   ├── RestauranteController.java
│   │   │               │   └── UsuarioController.java
│   │   │               ├── dto
│   │   │               │   ├── request
│   │   │               │   │   ├── AtualizarStatusPedidoRequest.java
│   │   │               │   │   ├── ClienteRequest.java
│   │   │               │   │   ├── ItemPedidoRequest.java
│   │   │               │   │   ├── LoginRequest.java
│   │   │               │   │   ├── PedidoRequest.java
│   │   │               │   │   ├── ProdutoExemploRequest.java
│   │   │               │   │   ├── ProdutoRequest.java
│   │   │               │   │   ├── RegisterRequest.java
│   │   │               │   │   ├── RestauranteRequest.java
│   │   │               │   │   └── UsuarioRequest.java
│   │   │               │   └── response
│   │   │               │       ├── ApiResponse.java
│   │   │               │       ├── ClienteResponse.java
│   │   │               │       ├── ErrorResponse.java
│   │   │               │       ├── ItemPedidoResponse.java
│   │   │               │       ├── PedidoResponse.java
│   │   │               │       ├── ProdutoResponse.java
│   │   │               │       ├── RegisterResponse.java
│   │   │               │       ├── RestauranteResponse.java
│   │   │               │       └── UsuarioResponse.java
│   │   │               ├── entregaveis
│   │   │               │   ├── JMeter-Plano-Teste.png
│   │   │               │   ├── docker.png
│   │   │               │   ├── entregaID-nao_existia.png
│   │   │               │   ├── grafana.png
│   │   │               │   ├── h2-console.png
│   │   │               │   ├── jmeter
│   │   │               │   │   ├── API-deliverytech.postman_collection.json
│   │   │               │   │   └── Plano de Teste da API Delivery.jmx
│   │   │               │   ├── monitoring
│   │   │               │   │   ├── JVM (Micrometer)-1753801377090.json
│   │   │               │   │   └── teste-carga.jmx
│   │   │               │   ├── postman-1.png
│   │   │               │   ├── postman-10.png
│   │   │               │   ├── postman-2.png
│   │   │               │   ├── postman-3.png
│   │   │               │   ├── postman-4.png
│   │   │               │   ├── postman-5.png
│   │   │               │   ├── postman-6.png
│   │   │               │   ├── postman-7.png
│   │   │               │   ├── postman-8.png
│   │   │               │   ├── postman-9.png
│   │   │               │   ├── postman-documentacao.png
│   │   │               │   ├── postman-documentacao.txt
│   │   │               │   ├── prometheus.png
│   │   │               │   ├── status-cliente_estava_desabilitado.png
│   │   │               │   ├── swagger-1.png
│   │   │               │   ├── swagger-2.png
│   │   │               │   └── swagger-3.png
│   │   │               ├── exception
│   │   │               │   ├── BusinessException.java
│   │   │               │   ├── ConflictException.java
│   │   │               │   ├── EntityNotFoundException.java
│   │   │               │   ├── ErrorResponse.java
│   │   │               │   └── GlobalExceptionHandler.java
│   │   │               ├── model
│   │   │               │   ├── Cliente.java
│   │   │               │   ├── Endereco.java
│   │   │               │   ├── Entrega.java
│   │   │               │   ├── Entregador.java
│   │   │               │   ├── ItemPedido.java
│   │   │               │   ├── Pedido.java
│   │   │               │   ├── Produto.java
│   │   │               │   ├── Restaurante.java
│   │   │               │   ├── Role.java
│   │   │               │   ├── StatusEntrega.java
│   │   │               │   ├── StatusEntregaCheck.java
│   │   │               │   ├── StatusPedido.java
│   │   │               │   └── Usuario.java
│   │   │               ├── repository
│   │   │               │   ├── ClienteRepository.java
│   │   │               │   ├── EntregaRepository.java
│   │   │               │   ├── EntregadorRepository.java
│   │   │               │   ├── PedidoRepository.java
│   │   │               │   ├── ProdutoRepository.java
│   │   │               │   ├── RestauranteRepository.java
│   │   │               │   └── UsuarioRepository.java
│   │   │               ├── security
│   │   │               │   ├── JwtAuthenticationFilter.java
│   │   │               │   ├── JwtUtil.java
│   │   │               │   ├── KeyGenerator.java
│   │   │               │   ├── PasswordEncoderGenerator.java
│   │   │               │   └── UsuarioDetailsServiceImpl.java
│   │   │               └── service
│   │   │                   ├── ClienteService.java
│   │   │                   ├── ClienteServiceImpl.java
│   │   │                   ├── CustomUserDetailsService.java
│   │   │                   ├── PedidoService.java
│   │   │                   ├── PedidoServiceImpl.java
│   │   │                   ├── ProdutoService.java
│   │   │                   ├── ProdutoServiceImpl.java
│   │   │                   ├── RestauranteService.java
│   │   │                   ├── RestauranteServiceImpl.java
│   │   │                   └── UsuarioService.java
│   │   └── resources
│   │       ├── application-dev.properties
│   │       ├── application-test.properties
│   │       └── application.properties
│   └── test
│       ├── java
│       │   └── com
│       │       └── deliverytech
│       │           └── api
│       │               ├── ApiApplicationTests.java
│       │               ├── config
│       │               │   └── SwaggerTestSecurityConfig.java
│       │               ├── controller
│       │               │   ├── AdminRegistrationTest.java
│       │               │   ├── AuthControllerIntegrationTest.java
│       │               │   ├── AuthControllerTest.java
│       │               │   ├── ClienteControllerTest.java
│       │               │   ├── EntregaControllerTest.java
│       │               │   ├── EntregadorControllerTest.java
│       │               │   ├── PedidoControllerTest.java
│       │               │   ├── ProdutoControllerTest.java
│       │               │   ├── ProdutoControllerTestNew.java
│       │               │   ├── RestauranteControllerTest.java
│       │               │   ├── RestauranteControllerTestSimples.java
│       │               │   ├── RestauranteControllerTestUnitario.java
│       │               │   ├── UsuarioControllerSimpleTest.java
│       │               │   ├── UsuarioControllerTest.java
│       │               │   └── UsuarioControllerUnitTest.java
│       │               ├── dto
│       │               │   ├── request
│       │               │   │   ├── ClienteRequestTest.java
│       │               │   │   ├── LoginRequestTest.java
│       │               │   │   ├── ProdutoRequestTest.java
│       │               │   │   └── RegisterRequestTest.java
│       │               │   └── response
│       │               │       ├── ApiResponseTest.java
│       │               │       ├── ClienteResponseTest.java
│       │               │       ├── ItemPedidoResponseTest.java
│       │               │       ├── PedidoResponseTest.java
│       │               │       ├── ProdutoResponseTest.java
│       │               │       ├── RestauranteResponseTest.java
│       │               │       └── UsuarioResponseTest.java
│       │               ├── exception
│       │               │   ├── BusinessExceptionTest.java
│       │               │   ├── ConflictExceptionTest.java
│       │               │   ├── EntityNotFoundExceptionTest.java
│       │               │   ├── ErrorResponseTest.java
│       │               │   └── GlobalExceptionHandlerTest.java
│       │               ├── model
│       │               │   ├── ClienteTest.java
│       │               │   ├── EnderecoTest.java
│       │               │   ├── EntregaTest.java
│       │               │   ├── EntregadorTest.java
│       │               │   ├── ItemPedidoTest.java
│       │               │   ├── PedidoTest.java
│       │               │   ├── ProdutoTest.java
│       │               │   ├── RestauranteTest.java
│       │               │   ├── RoleTest.java
│       │               │   ├── StatusEntregaTest.java
│       │               │   ├── StatusPedidoTest.java
│       │               │   └── UsuarioTest.java
│       │               ├── repository
│       │               │   ├── ClienteRepositoryTest.java
│       │               │   ├── EntregaRepositoryTest.java
│       │               │   ├── EntregadorRepositoryTest.java
│       │               │   ├── PedidoRepositoryTest.java
│       │               │   ├── ProdutoRepositoryTest.java
│       │               │   ├── RestauranteRepositoryTest.java
│       │               │   └── UsuarioRepositoryTest.java
│       │               ├── security
│       │               │   ├── JwtAuthenticationFilterTest.java
│       │               │   ├── JwtUtilTest.java
│       │               │   └── UsuarioDetailsServiceImplTest.java
│       │               ├── service
│       │               │   ├── ClienteServiceImplTest.java
│       │               │   ├── CustomUserDetailsServiceTest.java
│       │               │   ├── PedidoServiceImplTest.java
│       │               │   ├── ProdutoServiceImplTest.java
│       │               │   ├── RestauranteServiceImplTest.java
│       │               │   └── UsuarioServiceTest.java
│       │               └── test
│       │                   └── StatusEntregaDebug.java
│       └── resources
│           └── application-test.properties
├── teste_admin.json
├── teste_admin_com_restaurante.json
└── teste_restaurante.json

```

## 🚀 Tecnologias

- **Java 21 LTS** (versão mais recente)
- Spring Boot 3.2.x
- Spring Web
- Spring Data JPA
- H2 Database
- Maven
- Jmeter
- Docker
- Prometheus
- Grafana

## ⚡Recursos Modernos Utilizados

- Records (Java 14+)
- Text Blocks (Java 15+)
- Pattern Matching (Java 17+)
- Virtual Threads (Java 21)

## 🏃Como executar

1. **Pré-requisitos:** JDK 21 instalado
2. Clone o repositório
3. Execute: `./mvnw spring-boot:run` (no Linux) ou `mvn spring-boot:run` (no Windows)
4. Acesse: http://localhost:8081 (porta padrão da aplicação)

##🗒️Endpoints

- GET /actuator/health - Status da aplicação (se Actuator estiver habilitado)
- GET /swagger-ui.html - Documentação da API (Swagger UI)
- GET /v3/api-docs - Documentação OpenAPI em JSON
- GET /h2-console - Console do banco H2 (desenvolvimento)
- POST /api/v1/auth/login - Endpoint de login
- POST /api/v1/auth/register - Endpoint de registro

## 🔧 Acesso ao H2 Console

Para acessar o console do banco H2:

1. Certifique-se de que a aplicação está rodando
2. Acesse: http://localhost:8081/h2-console
3. Use as seguintes configurações:
   - **JDBC URL:** `jdbc:h2:mem:testdb`
   - **User Name:** `sa`
   - **Password:** `password`
4. Clique em "Connect"

## ⚙️Configuração

- Porta: 8081 (configurada no application.yml)
- Banco: H2 em memória
- Profile: development

## 👩‍💻 Desenvolvedor

[Karin] - [SDE-TI13]
Desenvolvido com JDK 21 e Spring Boot 3.2.x
