package com.deliverytech.api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.deliverytech.api.model.*;
import com.deliverytech.api.repository.*;

import java.math.BigDecimal;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initData(
            ClienteRepository clienteRepository,
            RestauranteRepository restauranteRepository,
            UsuarioRepository usuarioRepository,
            ProdutoRepository produtoRepository,
            PasswordEncoder passwordEncoder) {
        
        return args -> {
            // Só adiciona dados se o banco estiver vazio
            if (clienteRepository.count() == 0) {
                
                // Inserir clientes
                Cliente cliente1 = Cliente.builder()
                    .nome("João Silva")
                    .email("joao.silva@email.com")
                    .ativo(true)
                    .build();
                clienteRepository.save(cliente1);

                Cliente cliente2 = Cliente.builder()
                    .nome("Maria Oliveira")
                    .email("maria.o@email.com")
                    .ativo(true)
                    .build();
                clienteRepository.save(cliente2);

                // Inserir restaurantes
                Restaurante restaurante1 = Restaurante.builder()
                    .nome("Pizzaria do Zé")
                    .categoria("Pizzaria")
                    .taxaEntrega(new BigDecimal("5.00"))
                    .tempoEntregaMinutos(30)
                    .telefone("11234567890")
                    .ativo(true)
                    .build();
                restauranteRepository.save(restaurante1);

                Restaurante restaurante2 = Restaurante.builder()
                    .nome("Hamburgueria Top")
                    .categoria("Hamburgueria")
                    .taxaEntrega(new BigDecimal("7.50"))
                    .tempoEntregaMinutos(25)
                    .telefone("11876543210")
                    .ativo(true)
                    .build();
                restauranteRepository.save(restaurante2);

                // Inserir usuários
                Usuario admin = Usuario.builder()
                    .nome("Admin Sistema")
                    .email("admin@deliverytech.com")
                    .senha(passwordEncoder.encode("123456"))
                    .role(Role.ADMIN)
                    .ativo(true)
                    .build();
                usuarioRepository.save(admin);

                Usuario clienteUser = Usuario.builder()
                    .nome("João Silva")
                    .email("joao.silva@email.com")
                    .senha(passwordEncoder.encode("123456"))
                    .role(Role.CLIENTE)
                    .ativo(true)
                    .build();
                usuarioRepository.save(clienteUser);

                // Inserir produtos
                Produto pizza1 = Produto.builder()
                    .nome("Pizza Margherita")
                    .categoria("Pizza")
                    .descricao("Pizza com mussarela e manjericão")
                    .preco(new BigDecimal("45.00"))
                    .disponivel(true)
                    .restaurante(restaurante1)
                    .build();
                produtoRepository.save(pizza1);

                Produto pizza2 = Produto.builder()
                    .nome("Pizza Calabresa")
                    .categoria("Pizza")
                    .descricao("Pizza com calabresa e cebola")
                    .preco(new BigDecimal("50.00"))
                    .disponivel(true)
                    .restaurante(restaurante1)
                    .build();
                produtoRepository.save(pizza2);

                Produto burger1 = Produto.builder()
                    .nome("X-Burger Clássico")
                    .categoria("Hamburguer")
                    .descricao("Hamburguer com queijo, alface e tomate")
                    .preco(new BigDecimal("25.00"))
                    .disponivel(true)
                    .restaurante(restaurante2)
                    .build();
                produtoRepository.save(burger1);

            }
        };
    }
}
