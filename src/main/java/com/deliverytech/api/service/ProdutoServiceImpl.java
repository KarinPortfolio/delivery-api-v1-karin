package com.deliverytech.api.service;

import com.deliverytech.api.model.Produto;
import com.deliverytech.api.repository.ProdutoRepository; 
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service 
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository; 
    
    public ProdutoServiceImpl(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    } 
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
    @Transactional
    public void alterarDisponibilidade(Long id, boolean disponivel) {
        Optional<Produto> produtoOpt = produtoRepository.findById(id);
        if (produtoOpt.isPresent()) {
            Produto produto = produtoOpt.get();
            produto.setDisponivel(disponivel);
            produtoRepository.save(produto);
        }
        // Se o produto não existir, o método não faz nada (conforme implementação anterior)
    }
    }