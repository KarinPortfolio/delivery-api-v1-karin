package com.deliverytech.api.service;

import com.deliverytech.api.model.Produto;
import com.deliverytech.api.repository.ProdutoRepository; 
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service 
@RequiredArgsConstructor
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository; 
    @Override
    public Produto cadastrar(Produto produto) {
        return produtoRepository.save(produto);
    }

    @Override
    public Optional<Produto> buscarPorId(Long id) {
        return produtoRepository.findById(id);
    }

    @Override
    public List<Produto> buscarPorRestaurante(Long restauranteId) {
        
        return produtoRepository.findByRestauranteId(restauranteId);
    }

    @Override
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    @Override
    public Produto atualizar(Long id, Produto produtoAtualizado) {
        return produtoRepository.findById(id).map(produtoExistente -> {
            produtoExistente.setNome(produtoAtualizado.getNome());
            produtoExistente.setDescricao(produtoAtualizado.getDescricao());
            produtoExistente.setPreco(produtoAtualizado.getPreco());
            produtoExistente.setCategoria(produtoAtualizado.getCategoria());
            produtoExistente.setDisponivel(produtoAtualizado.getDisponivel());
            return produtoRepository.save(produtoExistente);
        }).orElseThrow(() -> new RuntimeException("Produto not found for update: " + id));
    }
    @Override
        public void alterarDisponibilidade(Long id, boolean disponivel) {
            produtoRepository.findById(id).ifPresent(produto -> {
                produto.setDisponivel(disponivel);
                produtoRepository.save(produto);
            });
        }
    }