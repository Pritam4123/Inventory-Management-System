package com.inventory.dto;

import java.time.LocalDateTime;

/**
 * Generic API Response wrapper.
 * Provides consistent response structure across the application.
 */
public class ApiResponse<T> {
    
    private boolean success;
    private String message;
    private T data;
    private String errorCode;
    private LocalDateTime timestamp;
    
    // Default constructor
    public ApiResponse() {
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor for success response with data
    public ApiResponse(T data) {
        this.success = true;
        this.data = data;
        this.message = "Operation successful";
        this.timestamp = LocalDateTime.now();
    }
    
    // Constructor for success response with message and data
    public ApiResponse(String message, T data) {
        this.success = true;
        this.message = message;
        this.data = data;
        this.timestamp = LocalDateTime.now();
    }
    
    /**
     * Create a success response.
     */
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data);
    }
    
    /**
     * Create a failure response.
     */
    public static <T> ApiResponse<T> failure(String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        return response;
    }
    
    /**
     * Create a failure response with error code.
     */
    public static <T> ApiResponse<T> failure(String message, String errorCode) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = false;
        response.message = message;
        response.errorCode = errorCode;
        return response;
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
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
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    @Override
    public String toString() {
        return "ApiResponse{" +
                "success=" + success +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", errorCode='" + errorCode + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
