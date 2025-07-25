-- data.sql - Dados iniciais simples para teste

-- Inserindo Clientes básicos
INSERT INTO cliente (nome, email, ativo, data_cadastro) VALUES
('João Silva', 'joao.silva@email.com', true, CURRENT_TIMESTAMP),
('Maria Oliveira', 'maria.o@email.com', true, CURRENT_TIMESTAMP);

-- Inserindo Restaurantes básicos
INSERT INTO restaurante (nome, categoria, taxa_entrega, tempo_entrega_minutos, ativo, data_cadastro, telefone) VALUES
('Pizzaria do Zé', 'Pizzaria', 5.00, 30, true, CURRENT_TIMESTAMP, '11234567890'),
('Hamburgueria Top', 'Hamburgueria', 7.50, 25, true, CURRENT_TIMESTAMP, '11876543210');

-- Inserindo Usuários básicos
INSERT INTO usuario (nome, email, senha, role, ativo, data_criacao) VALUES
('Admin Sistema', 'admin@deliverytech.com', '$2a$10$N.zmdr9k7OKYQpXdvqyQle7dNfVIZn0e7nFIqLyNImBB5HT6ZZ8vG', 'ADMIN', true, CURRENT_TIMESTAMP),
('João Silva', 'joao.silva@email.com', '$2a$10$N.zmdr9k7OKYQpXdvqyQle7dNfVIZn0e7nFIqLyNImBB5HT6ZZ8vG', 'CLIENTE', true, CURRENT_TIMESTAMP);

-- Inserindo Produtos básicos
INSERT INTO produto (nome, categoria, descricao, preco, disponivel, restaurante_id) VALUES
('Pizza Margherita', 'Pizza', 'Pizza com mussarela e manjericão', 45.00, true, 1),
('X-Burger', 'Hamburguer', 'Hamburguer com queijo', 25.00, true, 2);