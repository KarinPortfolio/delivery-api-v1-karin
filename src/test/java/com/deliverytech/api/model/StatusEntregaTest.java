package com.deliverytech.api.model;

// Test class for StatusEntrega enum
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

class StatusEntregaTest {

    @Test
    @DisplayName("Deve verificar se todos os status esperados estão definidos no enum StatusEntrega")
    void deveConterTodosOsStatusEsperados() {
        // Obtenha todos os valores do enum StatusEntrega
        StatusEntrega[] statuses = StatusEntrega.values();

        // Verifique se o número de status é o esperado
        assertEquals(5, statuses.length, "Deve haver 5 status definidos no enum StatusEntrega");

        // Verifique se cada status esperado está presente
        assertTrue(containsStatus(statuses, "PENDENTE"), "O status PENDENTE deve estar presente");
        assertTrue(containsStatus(statuses, "EM_ANDAMENTO"), "O status EM_ANDAMENTO deve estar presente");
        assertTrue(containsStatus(statuses, "ENTREGUE"), "O status ENTREGUE deve estar presente");
        assertTrue(containsStatus(statuses, "CANCELADA"), "O status CANCELADA deve estar presente");
        assertTrue(containsStatus(statuses, "ATRASADA"), "O status ATRASADA deve estar presente");
    }

    @Test
    @DisplayName("Deve verificar a representação em String de cada status")
    void deveVerificarRepresentacaoEmStringDosStatus() {
        // Teste o status PENDENTE
        assertEquals("PENDENTE", StatusEntrega.PENDENTE.name(), "A representação em String de PENDENTE deve ser 'PENDENTE'");
        assertEquals("PENDENTE", StatusEntrega.PENDENTE.toString(), "A representação em String de PENDENTE deve ser 'PENDENTE'");

        // Teste o status EM_ANDAMENTO
        assertEquals("EM_ANDAMENTO", StatusEntrega.EM_ANDAMENTO.name(), "A representação em String de EM_ANDAMENTO deve ser 'EM_ANDAMENTO'");
        assertEquals("EM_ANDAMENTO", StatusEntrega.EM_ANDAMENTO.toString(), "A representação em String de EM_ANDAMENTO deve ser 'EM_ANDAMENTO'");

        // Teste o status ENTREGUE
        StatusEntrega entregue = StatusEntrega.valueOf("ENTREGUE");
        assertEquals("ENTREGUE", entregue.name(), "A representação em String de ENTREGUE deve ser 'ENTREGUE'");
        assertEquals("ENTREGUE", entregue.toString(), "A representação em String de ENTREGUE deve ser 'ENTREGUE'");

        // Teste o status CANCELADA
        assertEquals("CANCELADA", StatusEntrega.CANCELADA.name(), "A representação em String de CANCELADA deve ser 'CANCELADA'");
        assertEquals("CANCELADA", StatusEntrega.CANCELADA.toString(), "A representação em String de CANCELADA deve ser 'CANCELADA'");

        // Teste o status ATRASADA
        StatusEntrega atrasada = StatusEntrega.valueOf("ATRASADA");
        assertEquals("ATRASADA", atrasada.name(), "A representação em String de ATRASADA deve ser 'ATRASADA'");
        assertEquals("ATRASADA", atrasada.toString(), "A representação em String de ATRASADA deve ser 'ATRASADA'");
    }

    @Test
    @DisplayName("Deve converter String para enum StatusEntrega corretamente")
    void deveConverterStringParaStatusEntregaCorretamente() {
        // Teste a conversão para PENDENTE
        assertEquals(StatusEntrega.PENDENTE, StatusEntrega.valueOf("PENDENTE"), "Deve converter 'PENDENTE' para StatusEntrega.PENDENTE");

        // Teste a conversão para EM_ANDAMENTO
        assertEquals(StatusEntrega.EM_ANDAMENTO, StatusEntrega.valueOf("EM_ANDAMENTO"), "Deve converter 'EM_ANDAMENTO' para StatusEntrega.EM_ANDAMENTO");

        // Teste a conversão para ENTREGUE
        assertEquals(StatusEntrega.valueOf("ENTREGUE"), StatusEntrega.valueOf("ENTREGUE"), "Deve converter 'ENTREGUE' para StatusEntrega.ENTREGUE");

        // Teste a conversão para CANCELADA
        assertEquals(StatusEntrega.CANCELADA, StatusEntrega.valueOf("CANCELADA"), "Deve converter 'CANCELADA' para StatusEntrega.CANCELADA");

        // Teste a conversão para ATRASADA
        assertEquals(StatusEntrega.valueOf("ATRASADA"), StatusEntrega.valueOf("ATRASADA"), "Deve converter 'ATRASADA' para StatusEntrega.ATRASADA");
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar converter String inválida para StatusEntrega")
    void deveLancarExcecaoParaStringInvalida() {
        // Tente converter uma string que não corresponde a nenhum status
        assertThrows(IllegalArgumentException.class, () -> StatusEntrega.valueOf("STATUS_INEXISTENTE"),
                "Deve lançar IllegalArgumentException para uma string de status inválida");
    }

    /**
     * Método auxiliar para verificar se um status está presente no array de StatusEntrega.
     * @param statuses Array de StatusEntrega.
     * @param statusName Nome do status a ser verificado.
     * @return true se o status estiver presente, false caso contrário.
     */
    private boolean containsStatus(StatusEntrega[] statuses, String statusName) {
        for (StatusEntrega status : statuses) {
            if (status.name().equals(statusName)) {
                return true;
            }
        }
        return false;
    }
}
