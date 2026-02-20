package com.inventory.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

/**
 * Unit tests for Product model class.
 */
public class ProductTest {
    
    @Test
    public void testProductCreation() {
        Product product = new Product(
            "Test Product",
            "A test product description",
            "Electronics",
            new BigDecimal("99.99"),
            50,
            10
        );
        
        assertNotNull(product);
        assertEquals("Test Product", product.getName());
        assertEquals("A test product description", product.getDescription());
        assertEquals("Electronics", product.getCategory());
        assertEquals(new BigDecimal("99.99"), product.getPrice());
        assertEquals(50, product.getQuantity());
        assertEquals(10, product.getLowStockThreshold());
    }
    
    @Test
    public void testIsLowStock() {
        Product product = new Product();
        product.setQuantity(5);
        product.setLowStockThreshold(10);
        
        assertTrue(product.isLowStock(), "Product should be low stock when quantity is below threshold");
    }
    
    @Test
    public void testIsOutOfStock() {
        Product product = new Product();
        product.setQuantity(0);
        
        assertTrue(product.isOutOfStock(), "Product should be out of stock when quantity is zero");
    }
    
    @Test
    public void testSetId() {
        Product product = new Product();
        product.setId(1);
        
        assertEquals(1, product.getId());
    }
}
