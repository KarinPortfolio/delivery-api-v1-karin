package com.deliverytech.api.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

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
  public ResponseEntity<ErrorResponse> handleConflictException(
      ConflictException ex, WebRequest request) {

    Map<String, String> details = new HashMap<>();
    if (ex.getConflictField() != null) {
      details.put(ex.getConflictField(), ex.getConflictValue().toString());
    }

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        "Conflito de dados",
        ex.getMessage(),
        request.getDescription(false).replace("uri=", ""));
    errorResponse.setErrorCode(ex.getErrorCode());
    errorResponse.setDetails(details.isEmpty() ? null : details);

    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGenericException(
      Exception ex, WebRequest request) {

    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR.value(),
        "Erro interno do servidor",
        "Ocorreu um erro inesperado. Tente novamente mais tarde.",
        request.getDescription(false).replace("uri=", ""));
    errorResponse.setErrorCode("INTERNAL_ERROR");

    return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}