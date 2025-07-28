// src/main/java/com/deliverytech/api/service/RestauranteServiceImpl.java (Implementação)
package com.deliverytech.api.service;

import com.deliverytech.api.exception.ConflictException;
import com.deliverytech.api.model.Produto;
import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.repository.ProdutoRepository;
import com.deliverytech.api.repository.RestauranteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe se usar transações
import org.springframework.web.server.ResponseStatusException; // Para lançar 404
import org.springframework.http.HttpStatus; // Para HttpStatus

import java.util.List;
import java.util.Optional;

@Service
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository restauranteRepository;
    private final ProdutoRepository produtoRepository;
    
    public RestauranteServiceImpl(RestauranteRepository restauranteRepository, ProdutoRepository produtoRepository) {
        this.restauranteRepository = restauranteRepository;
        this.produtoRepository = produtoRepository;
    }

    @Override
    @Transactional
    public Restaurante cadastrar(Restaurante restaurante) {
        // Verifica se já existe um restaurante com o mesmo nome
        Optional<Restaurante> restauranteExistente = restauranteRepository.findByNome(restaurante.getNome());
        if (restauranteExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um restaurante cadastrado com este nome: " + restaurante.getNome());
        }
        return restauranteRepository.save(restaurante);
    }

    @Override
    public List<Restaurante> listarTodos() {
        return restauranteRepository.findAll();
    }

    @Override
    public Optional<Restaurante> buscarPorId(Long id) {
        return restauranteRepository.findById(id);
    }

    @Override
    public List<Restaurante> buscarPorCategoria(String categoria) {
        return restauranteRepository.findByCategoria(categoria);
    }

    @Override
    @Transactional
    public Restaurante atualizar(Long id, Restaurante restauranteDetalhes) {
        Restaurante restauranteExistente = restauranteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado com ID: " + id));

        // Verifica se o novo nome já existe em outro restaurante
        if (restauranteDetalhes.getNome() != null && !restauranteDetalhes.getNome().equals(restauranteExistente.getNome())) {
            Optional<Restaurante> restauranteComMesmoNome = restauranteRepository.findByNome(restauranteDetalhes.getNome());
            if (restauranteComMesmoNome.isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe outro restaurante cadastrado com este nome: " + restauranteDetalhes.getNome());
            }
        }

        // Atualiza os campos necessários (adicione lógica para campos nulos se for atualização parcial)
        if (restauranteDetalhes.getNome() != null) restauranteExistente.setNome(restauranteDetalhes.getNome());
        if (restauranteDetalhes.getTelefone() != null) restauranteExistente.setTelefone(restauranteDetalhes.getTelefone());
        if (restauranteDetalhes.getCategoria() != null) restauranteExistente.setCategoria(restauranteDetalhes.getCategoria());
        if (restauranteDetalhes.getTaxaEntrega() != null) restauranteExistente.setTaxaEntrega(restauranteDetalhes.getTaxaEntrega());
        if (restauranteDetalhes.getTempoEntregaMinutos() != null) restauranteExistente.setTempoEntregaMinutos(restauranteDetalhes.getTempoEntregaMinutos());
        if (restauranteDetalhes.getAtivo() != null) restauranteExistente.setAtivo(restauranteDetalhes.getAtivo());

        return restauranteRepository.save(restauranteExistente);
    }

    @Override
    public boolean findByNome(String nome) {
        // Implementa a lógica para verificar se existe um restaurante com o nome
        return restauranteRepository.findByNome(nome).isPresent();
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!restauranteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado com ID: " + id);
        }
        
        // Verificar se existem produtos associados ao restaurante
        List<Produto> produtosAssociados = produtoRepository.findByRestauranteId(id);
        if (!produtosAssociados.isEmpty()) {
            throw new ConflictException(
                "Não é possível deletar o restaurante pois existem " + produtosAssociados.size() + 
                " produto(s) associado(s). Remova os produtos primeiro.",
                "restauranteId", 
                String.valueOf(id)
            );
        }
        
        restauranteRepository.deleteById(id);
    }
}