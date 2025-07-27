package com.deliverytech.api.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityNotFoundExceptionTest {

    @Test
    void deveTestarConstrutoroComEntityNameEId() {
        // Arrange
        String entityName = "Cliente";
        Long entityId = 1L;

        // Act
        EntityNotFoundException exception = new EntityNotFoundException(entityName, entityId);

        // Assert
        assertEquals("Cliente com ID 1 não foi encontrado(a)", exception.getMessage());
        assertEquals("ENTITY_NOT_FOUND", exception.getErrorCode());
        assertEquals(entityName, exception.getEntityName());
        assertEquals(entityId, exception.getEntityId());
    }

    @Test
    void deveTestarConstrutorComMensagem() {
        // Arrange
        String message = "Erro customizado";

        // Act
        EntityNotFoundException exception = new EntityNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals("ENTITY_NOT_FOUND", exception.getErrorCode());
    }

    @Test
    void deveTestarHerancaDeBussinessException() {
        // Act
        EntityNotFoundException exception = new EntityNotFoundException("Test", 1L);

        // Assert
        assertInstanceOf(BusinessException.class, exception);
        assertInstanceOf(RuntimeException.class, exception);
    }
}
