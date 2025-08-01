# API.md

## QualificaSP Delivery API – Endpoints Principais

---

### Autenticação

#### POST `/api/v1/auth/login`
- **Descrição:** Autentica usuário e retorna JWT.
- **Body:** `{ "email": "string", "senha": "string" }`
- **Response:** `{ "token": "jwt-token", "refreshToken": "..." }`

#### POST `/api/v1/auth/refresh-token`
- **Descrição:** Gera novo JWT a partir do refresh token.
- **Body:** `{ "refreshToken": "..." }`
- **Response:** `{ "token": "jwt-token" }`

---

### Usuários

#### GET `/api/v1/usuarios/{id}`
- **Descrição:** Detalhes de um usuário (ADMIN).
- **Auth:** Bearer JWT (role: ADMIN)

#### POST `/api/v1/usuarios`
- **Descrição:** Cria novo usuário (ADMIN).
- **Auth:** Bearer JWT (role: ADMIN)
- **Body:** `{ ...dados do usuário... }`

#### PUT `/api/v1/usuarios/{id}`
- **Descrição:** Atualiza usuário (ADMIN).
- **Auth:** Bearer JWT (role: ADMIN)
- **Body:** `{ ...dados do usuário... }`

#### DELETE `/api/v1/usuarios/{id}`
- **Descrição:** Remove usuário (ADMIN).
- **Auth:** Bearer JWT (role: ADMIN)

---

### Restaurantes

#### GET `/api/v1/restaurantes`
- **Descrição:** Lista restaurantes (todos os papéis).
- **Auth:** Bearer JWT

#### POST `/api/v1/restaurantes`
- **Descrição:** Cria restaurante (ADMIN, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT (roles: ADMIN, RESTAURANTE, ENTREGADOR)

#### PUT `/api/v1/restaurantes/{id}`
- **Descrição:** Atualiza restaurante (ADMIN, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT (roles: ADMIN, RESTAURANTE, ENTREGADOR)

#### DELETE `/api/v1/restaurantes/{id}`
- **Descrição:** Remove restaurante (ADMIN, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT (roles: ADMIN, RESTAURANTE, ENTREGADOR)

---

### Produtos

#### GET `/api/v1/produtos`
- **Descrição:** Lista produtos (todos os papéis).
- **Auth:** Bearer JWT

#### POST `/api/v1/produtos`
- **Descrição:** Cria produto (ADMIN, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT (roles: ADMIN, RESTAURANTE, ENTREGADOR)

#### PUT `/api/v1/produtos/{id}`
- **Descrição:** Atualiza produto (ADMIN, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT (roles: ADMIN, RESTAURANTE, ENTREGADOR)

#### DELETE `/api/v1/produtos/{id}`
- **Descrição:** Remove produto (ADMIN, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT (roles: ADMIN, RESTAURANTE, ENTREGADOR)

---

### Pedidos

#### GET `/api/v1/pedidos`
- **Descrição:** Lista pedidos (ADMIN, CLIENTE, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT

#### POST `/api/v1/pedidos`
- **Descrição:** Cria pedido (ADMIN, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT (roles: ADMIN, RESTAURANTE, ENTREGADOR)

#### PUT `/api/v1/pedidos/{id}`
- **Descrição:** Atualiza pedido (ADMIN, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT (roles: ADMIN, RESTAURANTE, ENTREGADOR)

#### DELETE `/api/v1/pedidos/{id}`
- **Descrição:** Remove pedido (ADMIN, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT (roles: ADMIN, RESTAURANTE, ENTREGADOR)

---

### Entregas

#### GET `/api/v1/entregas`
- **Descrição:** Lista entregas (ADMIN, CLIENTE, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT

#### POST `/api/v1/entregas`
- **Descrição:** Cria entrega (ADMIN, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT (roles: ADMIN, RESTAURANTE, ENTREGADOR)

#### PUT `/api/v1/entregas/{id}`
- **Descrição:** Atualiza entrega (ADMIN, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT (roles: ADMIN, RESTAURANTE, ENTREGADOR)

#### DELETE `/api/v1/entregas/{id}`
- **Descrição:** Remove entrega (ADMIN, RESTAURANTE, ENTREGADOR).
- **Auth:** Bearer JWT (roles: ADMIN, RESTAURANTE, ENTREGADOR)

---

### Observações Gerais
- Todos os endpoints (exceto login e refresh) exigem JWT válido.
- As roles são: `ADMIN`, `CLIENTE`, `RESTAURANTE`, `ENTREGADOR`.
- Para detalhes de payloads, consulte o Swagger UI ou os DTOs do projeto.
- Erros seguem padrão HTTP e retornam mensagens padronizadas.

---

Para mais detalhes, consulte o Swagger UI (`/swagger-ui.html`) ou o código fonte.
