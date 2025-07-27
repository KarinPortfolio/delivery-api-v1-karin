package com.deliverytech.api.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.deliverytech.api.dto.request.ClienteRequest;
import com.deliverytech.api.dto.response.ClienteResponse;
import com.deliverytech.api.model.Cliente;
import com.deliverytech.api.service.ClienteService;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @PostMapping
    public ResponseEntity<ClienteResponse> cadastrar(@RequestBody ClienteRequest request) {
        try {
            Cliente cliente = convertRequestToEntity(request);
            Cliente clienteSalvo = clienteService.cadastrar(cliente);
            ClienteResponse response = convertEntityToResponse(clienteSalvo);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", e);
        }
    }

    @GetMapping
    public ResponseEntity<List<ClienteResponse>> listar() {
        try {
            List<Cliente> clientes = clienteService.listarAtivos();
            List<ClienteResponse> response = clientes.stream()
                .map(this::convertEntityToResponse)
                .collect(Collectors.toList());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", e);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponse> buscarPorId(@PathVariable Long id) {
        try {
            Optional<Cliente> clienteOpt = clienteService.buscarPorId(id);
            if (clienteOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            Cliente cliente = clienteOpt.get();
            ClienteResponse response = convertEntityToResponse(cliente);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", e);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponse> atualizar(@PathVariable Long id, @RequestBody ClienteRequest request) {
        try {
            Cliente cliente = convertRequestToEntity(request);
            Cliente clienteAtualizado = clienteService.atualizar(id, cliente);
            ClienteResponse response = convertEntityToResponse(clienteAtualizado);
            return ResponseEntity.ok(response);
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", e);
        }
    }

    @PatchMapping("/{id}/ativar-desativar")
    public ResponseEntity<Void> ativarDesativar(@PathVariable Long id) {
        try {
            clienteService.ativarDesativar(id);
            return ResponseEntity.ok().build();
        } catch (ResponseStatusException e) {
            throw e;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro interno do servidor", e);
        }
    }

    // Métodos auxiliares para conversão (mantidos os mesmos)
    private Cliente convertRequestToEntity(ClienteRequest request) {
        Cliente cliente = new Cliente();
        cliente.setNome(request.getNome());
        cliente.setEmail(request.getEmail());
        return cliente;
    }

    private ClienteResponse convertEntityToResponse(Cliente cliente) {
        ClienteResponse response = new ClienteResponse();
        response.setId(cliente.getId());
        response.setNome(cliente.getNome());
        response.setEmail(cliente.getEmail());
        response.setAtivo(cliente.getAtivo());
        return response;
    }
}
