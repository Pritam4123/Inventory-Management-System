package com.inventory.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Unit tests for Sale model class.
 */
public class SaleTest {
    
    @Test
    public void testSaleCreation() {
        Sale sale = new Sale(1, 5, new BigDecimal("499.95"));
        
        assertNotNull(sale);
        assertEquals(1, sale.getProductId());
        assertEquals(5, sale.getQuantity());
        assertEquals(new BigDecimal("499.95"), sale.getTotalAmount());
    }
    
    @Test
    public void testSaleSetters() {
        Sale sale = new Sale();
        sale.setId(1);
        sale.setProductId(2);
        sale.setQuantity(10);
        sale.setTotalAmount(new BigDecimal("999.90"));
        
        assertEquals(1, sale.getId());
        assertEquals(2, sale.getProductId());
        assertEquals(10, sale.getQuantity());
        assertEquals(new BigDecimal("999.90"), sale.getTotalAmount());
    }
    
    @Test
    public void testSaleDate() {
        LocalDateTime now = LocalDateTime.now();
        Sale sale = new Sale(1, 5, new BigDecimal("499.95"));
        sale.setSaleDate(now);
        
        assertEquals(now, sale.getSaleDate());
    }
}
