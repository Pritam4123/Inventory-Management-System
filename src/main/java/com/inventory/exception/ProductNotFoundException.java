package com.inventory.exception;

/**
 * Custom exception for product not found situations.
 */
public class ProductNotFoundException extends RuntimeException {
    
    private int productId;
    
    public ProductNotFoundException(String message) {
        super(message);
    }
    
    public ProductNotFoundException(String message, int productId) {
        super(message);
        this.productId = productId;
    }
    
    public int getProductId() {
        return productId;
    }
}
