package com.deliverytech.api.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    @DisplayName("Deve verificar se todos os papéis esperados estão definidos no enum Role")
    void deveConterTodosOsPapeisEsperados() {
        // Obtenha todos os valores do enum Role
        Role[] roles = Role.values();

        // Verifique se o número de papéis é o esperado
        assertEquals(4, roles.length, "Deve haver 4 papéis definidos no enum Role");

        // Verifique se cada papel esperado está presente
        assertTrue(containsRole(roles, "CLIENTE"), "O papel CLIENTE deve estar presente");
        assertTrue(containsRole(roles, "RESTAURANTE"), "O papel RESTAURANTE deve estar presente");
        assertTrue(containsRole(roles, "ADMIN"), "O papel ADMIN deve estar presente");
        assertTrue(containsRole(roles, "ENTREGADOR"), "O papel ENTREGADOR deve estar presente");
    }

    @Test
    @DisplayName("Deve verificar a representação em String de cada papel")
    void deveVerificarRepresentacaoEmStringDosPapeis() {
        // Teste o papel CLIENTE
        assertEquals("CLIENTE", Role.CLIENTE.name(), "A representação em String de CLIENTE deve ser 'CLIENTE'");
        assertEquals("CLIENTE", Role.CLIENTE.toString(), "A representação em String de CLIENTE deve ser 'CLIENTE'");

        // Teste o papel RESTAURANTE
        assertEquals("RESTAURANTE", Role.RESTAURANTE.name(), "A representação em String de RESTAURANTE deve ser 'RESTAURANTE'");
        assertEquals("RESTAURANTE", Role.RESTAURANTE.toString(), "A representação em String de RESTAURANTE deve ser 'RESTAURANTE'");

        // Teste o papel ADMIN
        assertEquals("ADMIN", Role.ADMIN.name(), "A representação em String de ADMIN deve ser 'ADMIN'");
        assertEquals("ADMIN", Role.ADMIN.toString(), "A representação em String de ADMIN deve ser 'ADMIN'");

        // Teste o papel ENTREGADOR
        assertEquals("ENTREGADOR", Role.ENTREGADOR.name(), "A representação em String de ENTREGADOR deve ser 'ENTREGADOR'");
        assertEquals("ENTREGADOR", Role.ENTREGADOR.toString(), "A representação em String de ENTREGADOR deve ser 'ENTREGADOR'");
    }

    @Test
    @DisplayName("Deve converter String para enum Role corretamente")
    void deveConverterStringParaRoleCorretamente() {
        // Teste a conversão para CLIENTE
        assertEquals(Role.CLIENTE, Role.valueOf("CLIENTE"), "Deve converter 'CLIENTE' para Role.CLIENTE");

        // Teste a conversão para RESTAURANTE
        assertEquals(Role.RESTAURANTE, Role.valueOf("RESTAURANTE"), "Deve converter 'RESTAURANTE' para Role.RESTAURANTE");

        // Teste a conversão para ADMIN
        assertEquals(Role.ADMIN, Role.valueOf("ADMIN"), "Deve converter 'ADMIN' para Role.ADMIN");

        // Teste a conversão para ENTREGADOR
        assertEquals(Role.ENTREGADOR, Role.valueOf("ENTREGADOR"), "Deve converter 'ENTREGADOR' para Role.ENTREGADOR");
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar converter String inválida para Role")
    void deveLancarExcecaoParaStringInvalida() {
        // Tente converter uma string que não corresponde a nenhum papel
        assertThrows(IllegalArgumentException.class, () -> Role.valueOf("INVALID_ROLE"),
                "Deve lançar IllegalArgumentException para uma string de papel inválida");
    }

    /**
     * Método auxiliar para verificar se um papel está presente no array de Roles.
     * @param roles Array de Roles.
     * @param roleName Nome do papel a ser verificado.
     * @return true se o papel estiver presente, false caso contrário.
     */
    private boolean containsRole(Role[] roles, String roleName) {
        for (Role role : roles) {
            if (role.name().equals(roleName)) {
                return true;
            }
        }
        return false;
    }
}
