package com.deliverytech.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;

@RestController
@RequestMapping("/api/test")
@Tag(name = "Testes", description = "Endpoints para testar a API e verificar funcionamento")
public class TestController {

    @Operation(summary = "Teste simples", 
               description = "Endpoint básico para verificar se a API está funcionando")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "API funcionando corretamente",
                    content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Swagger está funcionando! 🎉");
    }
    
    @Operation(summary = "Status da API", 
               description = "Retorna o status atual da aplicação")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Status obtido com sucesso",
                    content = @Content(mediaType = "text/plain"))
    })
    @GetMapping("/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("API funcionando corretamente");
    }
}
