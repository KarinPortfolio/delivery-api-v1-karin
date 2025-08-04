# Visão Geral da Arquitetura

## Projeto: QualificaSP Delivery API

### 1. Visão Geral
Este projeto é uma API RESTful para uma plataforma de entrega de alimentos, desenvolvida com Java 21, Spring Boot e Spring Security. Ele oferece suporte à autenticação de usuários (JWT), controle de acesso baseado em papéis (roles) e ao gerenciamento de entidades como usuários, restaurantes, produtos, pedidos, entregas, e outras.

---

### 2. Tecnologias Principais
- **Java 21**
- **Spring Boot** (Web, Security, Data JPA)
- **JWT** para autenticação
- **H2** (dev/test) 
- **Maven** para build
- **Swagger/OpenAPI** para API documentação
- **JUnit/Mockito** para teste

---

### 3. Módulos e Camadas Principais

#### a. Controller Layer (`com.deliverytech.api.controller`)
- Expõe os endpoints REST para todos os recursos principais (autenticação, usuários, restaurantes, produtos, pedidos, entregas, etc.).

- Lida com as requisições/respostas HTTP e delega a lógica de negócio para os serviços.

#### b. Service Layer (`com.deliverytech.api.service`)
- Contém a lógica de negócio para cada domínio (ex: PedidoService, EntregaService, UsuarioService).

- Lida com validação, orquestração e chamadas aos repositórios.

#### c. Repository Layer (`com.deliverytech.api.repository`)
- Estende os repositórios do Spring Data JPA para operações CRUD e consultas.

- Mapeia as entidades para as tabelas do banco de dados.

#### d. Model Layer (`com.deliverytech.api.model`)
- Contém as entidades JPA: Usuario, Restaurante, Produto, Pedido, Entrega, etc.

- Modela relacionamentos (ex: OneToMany, ManyToOne, etc.) e restrições.

#### e. Security Layer (`com.deliverytech.api.security`)
- Filtro de autenticação JWT e utilitário (JwtAuthenticationFilter, JwtUtil).

- Implementação de UserDetailsService para buscar usuários.

- Configuração de segurança (SecurityConfig) para controle de acesso baseado em papéis.

#### f. Config Layer (`com.deliverytech.api.config`)
- Configuração do Swagger/OpenAPI, configuração de segurança e outras definições de toda a aplicação.

---

### 4. Security
- **Autenticação JWT**: Sem estado (stateless), com tokens emitidos no login e validados em cada requisição.

- **Acesso Baseado em Roles (Papéis)**: Os endpoints são protegidos por papéis (ADMIN, CLIENTE, RESTAURANTE, ENTREGADOR) usando @PreAuthorize ou SecurityConfig.

- **Criptografia de Senhas**: BCrypt.

---

### 5. Documentação da API
- **Swagger UI**  disponível em /swagger-ui.html (exceto no perfil de teste).
- Anotações OpenAPI documentam endpoints, modelos e requisitos de segurança.

---

### 6. Testes
- **JUnit 5** e **Mockito** para testes de unidade e integração.
- Cobertura de testes para controllers, serviços e segurança.

---

### 7. Tratamento de Erros
- Centralizado com @ControllerAdvice (ex: GlobalExceptionHandler).

- Retorna respostas de erro padronizadas para validação, erros de negócio e do sistema.
---

### 8. Banco de Dados
- **H2** em memória para desenvolvimento/teste.

- As entidades usam anotações JPA para mapeamento e restrições.

- Integridade referencial é imposta (ex: não é possível excluir um Pedido se ele for referenciado por uma Entrega).

---

### 9. Compilação e Execução
- Compilação: `mvn clean install`
- Execução: `mvn spring-boot:run` ou via Docker (see `Dockerfile`/`docker-compose.yml`)

---

### 10. Extensibilidade
- A estrutura modular permite fácil adição de novas funcionalidades (ex: pagamento, notificações).

- Segurança e validação são centralizadas para facilitar a manutenção.

---

### 11. Examplo de Roles (Papéis)
- **ADMIN**: Acesso total a todos os endpoints.
- **CLIENTE**: Pode visualizar e criar pedidos, visualizar produtos/restaurantes.
- **RESTAURANTE**: Gerencia seus próprios produtos, visualiza/gerencia pedidos.
- **ENTREGADOR**: Visualiza e atualiza as entregas atribuídas a ele.

---

### 12. Notas
- Endpoints sensíveis são protegidos por autenticação e verificação de papéis.

- As exclusões em cascata (cascade deletes) e a integridade referencial devem ser tratadas com cuidado na camada de serviço.
