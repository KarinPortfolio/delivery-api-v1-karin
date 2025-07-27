package com.deliverytech.api.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConflictExceptionTest {

    @Test
    void deveTestarConstrutorComMensagem() {
        // Arrange
        String message = "Conflito encontrado";

        // Act
        ConflictException exception = new ConflictException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals("CONFLICT", exception.getErrorCode());
    }

    @Test
    void deveTestarHerancaDeBussinessException() {
        // Act
        ConflictException exception = new ConflictException("Test");

        // Assert
        BusinessException businessException = assertInstanceOf(BusinessException.class, exception);
        RuntimeException runtimeException = assertInstanceOf(RuntimeException.class, exception);
        assertNotNull(businessException);
        assertNotNull(runtimeException);
    }
}
