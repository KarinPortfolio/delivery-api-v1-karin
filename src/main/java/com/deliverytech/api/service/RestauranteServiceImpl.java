// src/main/java/com/deliverytech/api/service/RestauranteServiceImpl.java (Implementação)
package com.deliverytech.api.service;

import com.deliverytech.api.model.Restaurante;
import com.deliverytech.api.repository.RestauranteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe se usar transações
import org.springframework.web.server.ResponseStatusException; // Para lançar 404
import org.springframework.http.HttpStatus; // Para HttpStatus

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RestauranteServiceImpl implements RestauranteService {

    private final RestauranteRepository restauranteRepository;

    @Override
    @Transactional
    public Restaurante cadastrar(Restaurante restaurante) {
        // Você pode adicionar a lógica de verificação de nome duplicado aqui no serviço
        // ou deixar no controller. Se for no serviço, o findByNome deve ser aqui.
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
        // Implemente a lógica para verificar se existe um restaurante com o nome
        // Ex: return restauranteRepository.findByNome(nome).isPresent();
        return false; // Retorno de exemplo
    }

    @Override
    @Transactional // <--- Adicione @Transactional para operações de escrita
    public void deletar(Long id) { // <--- NOVO MÉTODO: Implementação do método deletar
        if (!restauranteRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Restaurante não encontrado com ID: " + id);
        }
        restauranteRepository.deleteById(id);
    }
}