package com.inventory.exception;

/**
 * Custom exception for insufficient stock situations.
 */
public class InsufficientStockException extends RuntimeException {
    
    private int productId;
    private int requestedQuantity;
    private int availableQuantity;
    
    public InsufficientStockException(String message) {
        super(message);
    }
    
    public InsufficientStockException(String message, int productId, int requestedQuantity, int availableQuantity) {
        super(message);
        this.productId = productId;
        this.requestedQuantity = requestedQuantity;
        this.availableQuantity = availableQuantity;
    }
    
    public int getProductId() {
        return productId;
    }
    
    public int getRequestedQuantity() {
        return requestedQuantity;
    }
    
    public int getAvailableQuantity() {
        return availableQuantity;
    }
}
