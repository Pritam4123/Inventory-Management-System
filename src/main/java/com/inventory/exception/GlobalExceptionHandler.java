package com.inventory.exception;

import com.inventory.util.LoggerUtil;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Global Exception Handler for the Inventory Management System.
 * Provides centralized exception handling and consistent error responses.
 */
public class GlobalExceptionHandler {
    
    private static final LoggerUtil logger = LoggerUtil.getLogger(GlobalExceptionHandler.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    
    // Error codes
    public static final String ERR_PRODUCT_NOT_FOUND = "ERR_PRODUCT_NOT_FOUND";
    public static final String ERR_INSUFFICIENT_STOCK = "ERR_INSUFFICIENT_STOCK";
    public static final String ERR_DATABASE_ERROR = "ERR_DATABASE_ERROR";
    public static final String ERR_VALIDATION_ERROR = "ERR_VALIDATION_ERROR";
    public static final String ERR_INVALID_INPUT = "ERR_INVALID_INPUT";
    public static final String ERR_UNKNOWN_ERROR = "ERR_UNKNOWN_ERROR";
    
    /**
     * Error response container.
     */
    public static class ErrorResponse {
        private String errorCode;
        private String message;
        private String timestamp;
        private String details;
        
        public ErrorResponse(String errorCode, String message) {
            this.errorCode = errorCode;
            this.message = message;
            this.timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        }
        
        public ErrorResponse(String errorCode, String message, String details) {
            this.errorCode = errorCode;
            this.message = message;
            this.details = details;
            this.timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        }
        
        // Getters
        public String getErrorCode() { return errorCode; }
        public String getMessage() { return message; }
        public String getTimestamp() { return timestamp; }
        public String getDetails() { return details; }
        
        @Override
        public String toString() {
            return "ErrorResponse{" +
                    "errorCode='" + errorCode + '\'' +
                    ", message='" + message + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", details='" + details + '\'' +
                    '}';
        }
    }
    
    /**
     * Handle ProductNotFoundException.
     */
    public static ErrorResponse handleProductNotFoundException(ProductNotFoundException ex) {
        logger.error("Product not found: " + ex.getMessage());
        return new ErrorResponse(
            ERR_PRODUCT_NOT_FOUND,
            "Product not found",
            ex.getMessage()
        );
    }
    
    /**
     * Handle InsufficientStockException.
     */
    public static ErrorResponse handleInsufficientStockException(InsufficientStockException ex) {
        logger.error("Insufficient stock: " + ex.getMessage());
        return new ErrorResponse(
            ERR_INSUFFICIENT_STOCK,
            "Insufficient stock for this sale",
            "Available quantity: " + ex.getAvailableQuantity() + ", Requested: " + ex.getRequestedQuantity()
        );
    }
    
    /**
     * Handle DatabaseException.
     */
    public static ErrorResponse handleDatabaseException(DatabaseException ex) {
        logger.error("Database error: " + ex.getMessage(), ex);
        return new ErrorResponse(
            ERR_DATABASE_ERROR,
            "A database error occurred",
            "Please contact support if the problem persists"
        );
    }
    
    /**
     * Handle validation errors.
     */
    public static ErrorResponse handleValidationException(String message) {
        logger.warn("Validation error: " + message);
        return new ErrorResponse(
            ERR_VALIDATION_ERROR,
            "Validation failed",
            message
        );
    }
    
    /**
     * Handle illegal argument errors.
     */
    public static ErrorResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        logger.warn("Invalid input: " + ex.getMessage());
        return new ErrorResponse(
            ERR_INVALID_INPUT,
            "Invalid input provided",
            ex.getMessage()
        );
    }
    
    /**
     * Handle all other exceptions.
     */
    public static ErrorResponse handleGenericException(Exception ex) {
        logger.error("Unexpected error: " + ex.getMessage(), ex);
        return new ErrorResponse(
            ERR_UNKNOWN_ERROR,
            "An unexpected error occurred",
            "Please contact support if the problem persists"
        );
    }
    
    /**
     * Handle any exception and return appropriate error response.
     */
    public static ErrorResponse handleException(Exception ex) {
        if (ex instanceof ProductNotFoundException) {
            return handleProductNotFoundException((ProductNotFoundException) ex);
        } else if (ex instanceof InsufficientStockException) {
            return handleInsufficientStockException((InsufficientStockException) ex);
        } else if (ex instanceof DatabaseException) {
            return handleDatabaseException((DatabaseException) ex);
        } else if (ex instanceof IllegalArgumentException) {
            return handleIllegalArgumentException((IllegalArgumentException) ex);
        } else if (ex instanceof IllegalStateException) {
            return handleIllegalStateException((IllegalStateException) ex);
        } else {
            return handleGenericException(ex);
        }
    }
    
    /**
     * Handle IllegalStateException.
     */
    public static ErrorResponse handleIllegalStateException(IllegalStateException ex) {
        logger.error("Illegal state: " + ex.getMessage());
        return new ErrorResponse(
            ERR_INVALID_INPUT,
            "Invalid state",
            ex.getMessage()
        );
    }
    
    /**
     * Create a map of error responses for multiple validation errors.
     */
    public static Map<String, String> createValidationErrorMap(String field, String message) {
        Map<String, String> errors = new HashMap<>();
        errors.put(field, message);
        return errors;
    }
    
    /**
     * Create a map of error responses for multiple validation errors.
     */
    public static Map<String, String> createValidationErrorMap(Map<String, String> fieldErrors) {
        return new HashMap<>(fieldErrors);
    }
}
