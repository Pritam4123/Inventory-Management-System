package com.inventory.util;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Comprehensive Validation Utility for the Inventory Management System.
 * Provides validation methods for common data types and business rules.
 */
public class ValidationUtil {
    
    // Common validation patterns
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"
    );
    
    private static final Pattern PHONE_PATTERN = Pattern.compile(
        "^[0-9]{10,15}$"
    );
    
    private static final Pattern ALPHANUMERIC_PATTERN = Pattern.compile(
        "^[A-Za-z0-9]+$"
    );
    
    // Validation result container
    public static class ValidationResult {
        private final boolean valid;
        private final List<String> errors;
        
        private ValidationResult(boolean valid, List<String> errors) {
            this.valid = valid;
            this.errors = errors;
        }
        
        public static ValidationResult success() {
            return new ValidationResult(true, new ArrayList<>());
        }
        
        public static ValidationResult failure(String error) {
            List<String> errors = new ArrayList<>();
            errors.add(error);
            return new ValidationResult(false, errors);
        }
        
        public static ValidationResult failure(List<String> errors) {
            return new ValidationResult(false, errors);
        }
        
        public boolean isValid() {
            return valid;
        }
        
        public List<String> getErrors() {
            return errors;
        }
        
        public String getErrorMessage() {
            return String.join(", ", errors);
        }
    }
    
    /**
     * Validate that a string is not null or empty.
     */
    public static ValidationResult validateNotEmpty(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return ValidationResult.failure(fieldName + " cannot be empty");
        }
        return ValidationResult.success();
    }
    
    /**
     * Validate string length.
     */
    public static ValidationResult validateLength(String value, String fieldName, int minLength, int maxLength) {
        if (value == null) {
            return ValidationResult.failure(fieldName + " cannot be null");
        }
        if (value.length() < minLength) {
            return ValidationResult.failure(fieldName + " must be at least " + minLength + " characters");
        }
        if (value.length() > maxLength) {
            return ValidationResult.failure(fieldName + " must be at most " + maxLength + " characters");
        }
        return ValidationResult.success();
    }
    
    /**
     * Validate email format.
     */
    public static ValidationResult validateEmail(String email, String fieldName) {
        if (email == null || email.trim().isEmpty()) {
            return ValidationResult.failure(fieldName + " cannot be empty");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            return ValidationResult.failure(fieldName + " is not a valid email address");
        }
        return ValidationResult.success();
    }
    
    /**
     * Validate phone number.
     */
    public static ValidationResult validatePhone(String phone, String fieldName) {
        if (phone == null || phone.trim().isEmpty()) {
            return ValidationResult.failure(fieldName + " cannot be empty");
        }
        // Remove spaces and dashes for validation
        String cleanedPhone = phone.replaceAll("[\\s-]", "");
        if (!PHONE_PATTERN.matcher(cleanedPhone).matches()) {
            return ValidationResult.failure(fieldName + " is not a valid phone number");
        }
        return ValidationResult.success();
    }
    
    /**
     * Validate that a number is positive.
     */
    public static ValidationResult validatePositive(BigDecimal value, String fieldName) {
        if (value == null) {
            return ValidationResult.failure(fieldName + " cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            return ValidationResult.failure(fieldName + " must be positive");
        }
        return ValidationResult.success();
    }
    
    /**
     * Validate that a number is non-negative.
     */
    public static ValidationResult validateNonNegative(BigDecimal value, String fieldName) {
        if (value == null) {
            return ValidationResult.failure(fieldName + " cannot be null");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            return ValidationResult.failure(fieldName + " cannot be negative");
        }
        return ValidationResult.success();
    }
    
    /**
     * Validate that an integer is positive.
     */
    public static ValidationResult validatePositive(Integer value, String fieldName) {
        if (value == null) {
            return ValidationResult.failure(fieldName + " cannot be null");
        }
        if (value <= 0) {
            return ValidationResult.failure(fieldName + " must be positive");
        }
        return ValidationResult.success();
    }
    
    /**
     * Validate that an integer is non-negative.
     */
    public static ValidationResult validateNonNegative(Integer value, String fieldName) {
        if (value == null) {
            return ValidationResult.failure(fieldName + " cannot be null");
        }
        if (value < 0) {
            return ValidationResult.failure(fieldName + " cannot be negative");
        }
        return ValidationResult.success();
    }
    
    /**
     * Validate that a value is within a range.
     */
    public static ValidationResult validateRange(Integer value, String fieldName, int min, int max) {
        if (value == null) {
            return ValidationResult.failure(fieldName + " cannot be null");
        }
        if (value < min || value > max) {
            return ValidationResult.failure(fieldName + " must be between " + min + " and " + max);
        }
        return ValidationResult.success();
    }
    
    /**
     * Validate that a value is within a range for BigDecimal.
     */
    public static ValidationResult validateRange(BigDecimal value, String fieldName, BigDecimal min, BigDecimal max) {
        if (value == null) {
            return ValidationResult.failure(fieldName + " cannot be null");
        }
        if (value.compareTo(min) < 0 || value.compareTo(max) > 0) {
            return ValidationResult.failure(fieldName + " must be between " + min + " and " + max);
        }
        return ValidationResult.success();
    }
    
    /**
     * Validate alphanumeric string.
     */
    public static ValidationResult validateAlphanumeric(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return ValidationResult.failure(fieldName + " cannot be empty");
        }
        if (!ALPHANUMERIC_PATTERN.matcher(value).matches()) {
            return ValidationResult.failure(fieldName + " must contain only letters and numbers");
        }
        return ValidationResult.success();
    }
    
    /**
     * Validate that a value is not null.
     */
    public static <T> ValidationResult validateNotNull(T value, String fieldName) {
        if (value == null) {
            return ValidationResult.failure(fieldName + " cannot be null");
        }
        return ValidationResult.success();
    }
    
    /**
     * Validate product-specific business rules.
     */
    public static ValidationResult validateProduct(String name, BigDecimal price, Integer quantity, Integer lowStockThreshold) {
        List<String> errors = new ArrayList<>();
        
        // Validate name
        ValidationResult nameResult = validateNotEmpty(name, "Product name");
        if (!nameResult.isValid()) {
            errors.addAll(nameResult.getErrors());
        }
        
        // Validate price
        ValidationResult priceResult = validatePositive(price, "Product price");
        if (!priceResult.isValid()) {
            errors.addAll(priceResult.getErrors());
        }
        
        // Validate quantity
        ValidationResult quantityResult = validateNonNegative(quantity, "Product quantity");
        if (!quantityResult.isValid()) {
            errors.addAll(quantityResult.getErrors());
        }
        
        // Validate low stock threshold
        ValidationResult thresholdResult = validateNonNegative(lowStockThreshold, "Low stock threshold");
        if (!thresholdResult.isValid()) {
            errors.addAll(thresholdResult.getErrors());
        }
        
        if (errors.isEmpty()) {
            return ValidationResult.success();
        } else {
            return ValidationResult.failure(errors);
        }
    }
    
    /**
     * Validate sale-specific business rules.
     */
    public static ValidationResult validateSale(Integer productId, Integer quantity) {
        List<String> errors = new ArrayList<>();
        
        // Validate product ID
        ValidationResult productResult = validatePositive(productId, "Product ID");
        if (!productResult.isValid()) {
            errors.addAll(productResult.getErrors());
        }
        
        // Validate quantity
        ValidationResult quantityResult = validatePositive(quantity, "Sale quantity");
        if (!quantityResult.isValid()) {
            errors.addAll(quantityResult.getErrors());
        }
        
        if (errors.isEmpty()) {
            return ValidationResult.success();
        } else {
            return ValidationResult.failure(errors);
        }
    }
    
    /**
     * Sanitize string input to prevent injection attacks.
     */
    public static String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        // Remove potentially dangerous characters
        return input.replaceAll("[<>\"'&]", "");
    }
    
    /**
     * Validate and sanitize string input.
     */
    public static ValidationResult validateAndSanitize(String value, String fieldName) {
        ValidationResult result = validateNotEmpty(value, fieldName);
        if (result.isValid()) {
            String sanitized = sanitizeInput(value);
            return ValidationResult.success();
        }
        return result;
    }
}
