package com.deliverytech.api.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta padrão da API contendo informações de sucesso ou erro")
public class ApiResponse<T> {

    @Schema(description = "Código de status HTTP", example = "200")
    private int status;

    @Schema(description = "Mensagem descritiva da resposta", example = "Operação realizada com sucesso")
    private String message;

    @Schema(description = "Dados da resposta (quando aplicável)")
    private T data;

    @Schema(description = "Timestamp da resposta", example = "2025-01-27T10:30:00Z")
    private String timestamp;

    // Constructors
    public ApiResponse() {
        this.timestamp = java.time.Instant.now().toString();
    }

    public ApiResponse(int status, String message) {
        this();
        this.status = status;
        this.message = message;
    }

    public ApiResponse(int status, String message, T data) {
        this(status, message);
        this.data = data;
    }

    // Static factory methods para facilitar uso
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(200, message, data);
    }

    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<>(200, message);
    }

    public static <T> ApiResponse<T> error(int status, String message) {
        return new ApiResponse<>(status, message);
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
