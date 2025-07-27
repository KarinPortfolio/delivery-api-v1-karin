package com.deliverytech.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class StatusPedidoTest {

    @Test
    @DisplayName("Deve verificar a representação em String de cada status")
    void deveVerificarRepresentacaoEmStringDosStatus() {
        // Teste o status CRIADO
        assertEquals("CRIADO", StatusPedido.CRIADO.name(), "A representação em String de CRIADO deve ser 'CRIADO'");
        assertEquals("CRIADO", StatusPedido.CRIADO.toString(), "A representação em String de CRIADO deve ser 'CRIADO'");

        // Teste o status CONFIRMADO
        assertEquals("CONFIRMADO", StatusPedido.CONFIRMADO.name(), "A representação em String de CONFIRMADO deve ser 'CONFIRMADO'");
        assertEquals("CONFIRMADO", StatusPedido.CONFIRMADO.toString(), "A representação em String de CONFIRMADO deve ser 'CONFIRMADO'");

        // Teste o status EM_PREPARACAO
        assertEquals("EM_PREPARACAO", StatusPedido.EM_PREPARACAO.name(), "A representação em String de EM_PREPARACAO deve ser 'EM_PREPARACAO'");
        assertEquals("EM_PREPARACAO", StatusPedido.EM_PREPARACAO.toString(), "A representação em String de EM_PREPARACAO deve ser 'EM_PREPARACAO'");

        // Teste o status ENVIADO
        assertEquals("ENVIADO", StatusPedido.ENVIADO.name(), "A representação em String de ENVIADO deve ser 'ENVIADO'");
        assertEquals("ENVIADO", StatusPedido.ENVIADO.toString(), "A representação em String de ENVIADO deve ser 'ENVIADO'");

        // Teste o status ENTREGUE
        assertEquals("ENTREGUE", StatusPedido.ENTREGUE.name(), "A representação em String de ENTREGUE deve ser 'ENTREGUE'");
        assertEquals("ENTREGUE", StatusPedido.ENTREGUE.toString(), "A representação em String de ENTREGUE deve ser 'ENTREGUE'");

        // Teste o status CANCELADO
        assertEquals("CANCELADO", StatusPedido.CANCELADO.name(), "A representação em String de CANCELADO deve ser 'CANCELADO'");
        assertEquals("CANCELADO", StatusPedido.CANCELADO.toString(), "A representação em String de CANCELADO deve ser 'CANCELADO'");
    }

    @Test
    @DisplayName("Deve converter String para enum StatusPedido corretamente")
    void deveConverterStringParaStatusPedidoCorretamente() {
        // Teste a conversão para CRIADO
        assertEquals(StatusPedido.CRIADO, StatusPedido.valueOf("CRIADO"), "Deve converter 'CRIADO' para StatusPedido.CRIADO");

        // Teste a conversão para CONFIRMADO
        assertEquals(StatusPedido.CONFIRMADO, StatusPedido.valueOf("CONFIRMADO"), "Deve converter 'CONFIRMADO' para StatusPedido.CONFIRMADO");

        // Teste a conversão para EM_PREPARACAO
        assertEquals(StatusPedido.EM_PREPARACAO, StatusPedido.valueOf("EM_PREPARACAO"), "Deve converter 'EM_PREPARACAO' para StatusPedido.EM_PREPARACAO");

        // Teste a conversão para ENVIADO
        assertEquals(StatusPedido.ENVIADO, StatusPedido.valueOf("ENVIADO"), "Deve converter 'ENVIADO' para StatusPedido.ENVIADO");

        // Teste a conversão para ENTREGUE
        assertEquals(StatusPedido.ENTREGUE, StatusPedido.valueOf("ENTREGUE"), "Deve converter 'ENTREGUE' para StatusPedido.ENTREGUE");

        // Teste a conversão para CANCELADO
        assertEquals(StatusPedido.CANCELADO, StatusPedido.valueOf("CANCELADO"), "Deve converter 'CANCELADO' para StatusPedido.CANCELADO");
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar converter String inválida para StatusPedido")
    void deveLancarExcecaoParaStringInvalida() {
        // Tente converter uma string que não corresponde a nenhum status
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> StatusPedido.valueOf("STATUS_INEXISTENTE"),
                "Deve lançar IllegalArgumentException para uma string de status inválida");
        
        // Verificar se a exceção contém informações úteis
        assertEquals("No enum constant com.deliverytech.api.model.StatusPedido.STATUS_INEXISTENTE", exception.getMessage());
    }
}
