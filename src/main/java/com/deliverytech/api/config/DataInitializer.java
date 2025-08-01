package com.deliverytech.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.deliverytech.api.model.*;
import com.deliverytech.api.repository.*;
import jakarta.annotation.Generated;
import java.math.BigDecimal;

@Configuration
public class DataInitializer {
@Generated (value = "DataInitializer", date = "2023-10-01T12:00:00Z")
    @Bean
    CommandLineRunner initData(
            ClienteRepository clienteRepository,
            RestauranteRepository restauranteRepository,
            UsuarioRepository usuarioRepository,
            ProdutoRepository produtoRepository,
            PedidoRepository pedidoRepository,
            EntregaRepository entregaRepository,
            EntregadorRepository entregadorRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            System.out.println("=== INICIANDO DataInitializer ===");
            
            // Garantir que o usuário admin sempre existe
            if (usuarioRepository.findByEmail("admin@deliverytech.com").isEmpty()) {
                System.out.println("Criando usuário admin...");
                Usuario admin = new Usuario();
                admin.setNome("Admin Sistema");
                admin.setEmail("admin@deliverytech.com");
                admin.setSenha(passwordEncoder.encode("123456"));
                admin.setRole(Role.ADMIN);
                admin.setAtivo(true);
                Usuario savedAdmin = usuarioRepository.save(admin);
                System.out.println("✓ Usuário admin criado com ID: " + savedAdmin.getId() + ", Ativo: " + savedAdmin.getAtivo());
            } else {
                System.out.println("✓ Usuário admin já existe");
            }

            // Verificar se há outros dados
            long clienteCount = clienteRepository.count();
            System.out.println("Total de clientes no banco: " + clienteCount);

            // Só adiciona outros dados se o banco estiver vazio
            if (clienteCount == 0) {
                System.out.println("Banco vazio, criando dados iniciais...");
                
                // Inserir clientes
                Cliente cliente1 = new Cliente();
                cliente1.setNome("João Silva");
                cliente1.setEmail("joao.silva@email.com");
                cliente1.setAtivo(true);
                cliente1 = clienteRepository.save(cliente1);

                Cliente cliente2 = new Cliente();
                cliente2.setNome("Maria Oliveira");
                cliente2.setEmail("maria.o@email.com");
                cliente2.setAtivo(true);
                cliente2 = clienteRepository.save(cliente2);

                // Inserir restaurantes
                Restaurante restaurante1 = new Restaurante();
                restaurante1.setNome("Pizzaria do Zé");
                restaurante1.setCategoria("Pizzaria");
                restaurante1.setTaxaEntrega(new BigDecimal("5.00"));
                restaurante1.setTempoEntregaMinutos(30);
                restaurante1.setTelefone("11234567890");
                restaurante1.setAtivo(true);
                restaurante1 = restauranteRepository.save(restaurante1);

                Restaurante restaurante2 = new Restaurante();
                restaurante2.setNome("Hamburgueria Top");
                restaurante2.setCategoria("Hamburgueria");
                restaurante2.setTaxaEntrega(new BigDecimal("7.50"));
                restaurante2.setTempoEntregaMinutos(25);
                restaurante2.setTelefone("11876543210");
                restaurante2.setAtivo(true);
                restaurante2 = restauranteRepository.save(restaurante2);

                // Inserir usuários (exceto admin que já foi criado acima)
                Usuario clienteUser = new Usuario();
                clienteUser.setNome("João Silva");
                clienteUser.setEmail("joao.silva@email.com");
                clienteUser.setSenha(passwordEncoder.encode("123456"));
                clienteUser.setRole(Role.CLIENTE);
                clienteUser.setAtivo(true);
                usuarioRepository.save(clienteUser);

                // Inserir produtos
                Produto pizza1 = new Produto();
                pizza1.setNome("Pizza Margherita");
                pizza1.setCategoria("Pizza");
                pizza1.setDescricao("Pizza com mussarela e manjericão");
                pizza1.setPreco(new BigDecimal("45.00"));
                pizza1.setDisponivel(true);
                pizza1.setRestaurante(restaurante1);
                produtoRepository.save(pizza1);

                Produto pizza2 = new Produto();
                pizza2.setNome("Pizza Calabresa");
                pizza2.setCategoria("Pizza");
                pizza2.setDescricao("Pizza com calabresa e cebola");
                pizza2.setPreco(new BigDecimal("50.00"));
                pizza2.setDisponivel(true);
                pizza2.setRestaurante(restaurante1);
                produtoRepository.save(pizza2);

                Produto burger1 = new Produto();
                burger1.setNome("X-Burger Clássico");
                burger1.setCategoria("Hamburguer");
                burger1.setDescricao("Hamburguer com queijo, alface e tomate");
                burger1.setPreco(new BigDecimal("25.00"));
                burger1.setDisponivel(true);
                burger1.setRestaurante(restaurante2);
                produtoRepository.save(burger1);

                // Inserir entregador
                Entregador entregador1 = new Entregador();
                entregador1.setNome("Carlos Motoboy");
                entregador1.setTelefone("11999999999");
                entregador1.setAtivo(true);
                entregador1 = entregadorRepository.save(entregador1);

                // Inserir pedido
                Pedido pedido1 = new Pedido();
                pedido1.setCliente(cliente1);
                pedido1.setRestaurante(restaurante1);
                pedido1.setStatus(StatusPedido.CRIADO);
                pedido1.setTotal(pizza1.getPreco());
                Endereco endereco = new Endereco(
                    "Rua das Flores",
                    "123",
                    "Centro",
                    "São Paulo",
                    "SP",
                    "01000-000"
                );
                pedido1.setEnderecoEntrega(endereco);
                pedido1 = pedidoRepository.save(pedido1);

                // Inserir item de pedido
                ItemPedido itemPedido1 = new ItemPedido();
                itemPedido1.setPedido(pedido1);
                itemPedido1.setProduto(pizza1);
                itemPedido1.setQuantidade(1);
                itemPedido1.setPrecoUnitario(pizza1.getPreco());
                // Se houver um repository para ItemPedido, salve:
                // itemPedidoRepository.save(itemPedido1);
                // Se for cascade pelo Pedido, adicione ao pedido e salve o pedido novamente:
                if (pedido1.getItens() != null) {
                    pedido1.getItens().add(itemPedido1);
                }
                else {
                    java.util.List<ItemPedido> itens = new java.util.ArrayList<>();
                    itens.add(itemPedido1);
                    pedido1.setItens(itens);
                }
                pedidoRepository.save(pedido1);

                // Inserir entrega
                Entrega entrega1 = new Entrega();
                entrega1.setPedido(pedido1);
                entrega1.setEntregador(entregador1);
                entrega1.setStatus(StatusEntrega.PENDENTE);
                entregaRepository.save(entrega1);

                System.out.println("✓ Dados iniciais criados com sucesso!");
            } else {
                System.out.println("✓ Banco já contém dados, pulando inicialização");
            }
            
            System.out.println("=== DataInitializer CONCLUÍDO ===");
        };
    }
}
