package com.deliverytech.api.exception;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationExceptions(
      MethodArgumentNotValidException ex, WebRequest request) {

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Dados inválidos",
        "Erro de validação nos dados enviados",
        request.getDescription(false).replace("uri=", ""));
    errorResponse.setErrorCode("VALIDATION_ERROR");
    errorResponse.setDetails(errors);

    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleEntityNotFoundException(
      EntityNotFoundException ex, WebRequest request) {

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        "Entidade não encontrada",
        ex.getMessage(),
        request.getDescription(false).replace("uri=", ""));
    errorResponse.setErrorCode(ex.getErrorCode());

    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
  }

  @ExceptionHandler(ConflictException.class)
  public ResponseEntity<Map<String, Object>> handleConflictException(
      ConflictException ex, WebRequest request) {

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", java.time.LocalDateTime.now());
    errorResponse.put("status", HttpStatus.CONFLICT.value());
    errorResponse.put("error", "Conflict");
    errorResponse.put("message", ex.getMessage());
    errorResponse.put("path", request.getDescription(false).replace("uri=", ""));
    
    if (ex.getConflictField() != null) {
      errorResponse.put("field", ex.getConflictField());
      errorResponse.put("value", ex.getConflictValue());
    }

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

    @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception ex, WebRequest request) {

    // Log completo da exceção para debug
    logger.error("=== GLOBALEXCEPTIONHANDLER DEBUG ===");
    logger.error("URL: {}", request.getDescription(false));
    logger.error("Exceção: {}", ex.getClass().getName());
    logger.error("Mensagem: {}", ex.getMessage());
    logger.error("Stack trace:", ex);
    logger.error("=== FIM DEBUG ===");

    // Se for o endpoint de login, vamos re-lançar a exceção para ver o erro real
    if (request.getDescription(false).contains("/auth/login")) {
      logger.error("Re-lançando exceção do login para debug...");
      throw new RuntimeException("Erro no login: " + ex.getMessage(), ex);
    }
    
    // Não capturar exceções de segurança/autenticação - verificar pelo nome da classe
    String className = ex.getClass().getName();
    if (className.contains("AuthenticationException") ||
        className.contains("AccessDeniedException") ||
        ex instanceof ResponseStatusException) {
      throw new RuntimeException(ex); // Re-lançar para ser tratada pelo controller específico
    }

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Erro interno do servidor",
        "Ocorreu um erro inesperado. Tente novamente mais tarde.",
        request.getDescription(false).replace("uri=", ""));
    errorResponse.setErrorCode("INTERNAL_ERROR");

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}