package com.inventory.service;

import com.inventory.dao.ProductDAO;
import com.inventory.dao.SaleDAO;
import com.inventory.exception.InsufficientStockException;
import com.inventory.exception.ProductNotFoundException;
import com.inventory.model.Product;
import com.inventory.model.RevenueSummary;
import com.inventory.model.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SalesService class.
 */
@ExtendWith(MockitoExtension.class)
public class SalesServiceTest {
    
    @Mock
    private SaleDAO saleDAO;
    
    @Mock
    private ProductDAO productDAO;
    
    @InjectMocks
    private SalesService salesService;
    
    private Product testProduct;
    private Sale testSale;
    
    @BeforeEach
    public void setUp() {
        salesService = new SalesService(saleDAO, productDAO);
        
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setCategory("Electronics");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setQuantity(50);
        testProduct.setLowStockThreshold(10);
        
        testSale = new Sale(1, 5, new BigDecimal("499.95"));
        testSale.setId(1);
    }
    
    @Test
    public void testProcessSale_Success() {
        when(productDAO.getById(1)).thenReturn(testProduct);
        when(saleDAO.create(any(Sale.class))).thenReturn(testSale);
        when(productDAO.updateQuantity(anyInt(), anyInt())).thenReturn(true);
        
        Sale result = salesService.processSale(1, 5);
        
        assertNotNull(result);
        assertEquals(1, result.getProductId());
        assertEquals(5, result.getQuantitySold());
        verify(productDAO, times(1)).updateQuantity(1, 45); // 50 - 5 = 45
    }
    
    @Test
    public void testProcessSale_ProductNotFound() {
        when(productDAO.getById(999)).thenReturn(null);
        
        assertThrows(ProductNotFoundException.class, () -> {
            salesService.processSale(999, 5);
        });
    }
    
    @Test
    public void testProcessSale_InsufficientStock() {
        testProduct.setQuantity(3); // Less than requested quantity
        
        when(productDAO.getById(1)).thenReturn(testProduct);
        
        assertThrows(InsufficientStockException.class, () -> {
            salesService.processSale(1, 5);
        });
        
        verify(saleDAO, never()).create(any(Sale.class));
        verify(productDAO, never()).updateQuantity(anyInt(), anyInt());
    }
    
    @Test
    public void testGetSale() {
        when(saleDAO.getById(1)).thenReturn(testSale);
        
        Sale result = salesService.getSale(1);
        
        assertNotNull(result);
        assertEquals(1, result.getId());
    }
    
    @Test
    public void testGetAllSales() {
        List<Sale> sales = Arrays.asList(testSale);
        when(saleDAO.getAll()).thenReturn(sales);
        
        List<Sale> result = salesService.getAllSales();
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testGetSalesByProduct() {
        List<Sale> sales = Arrays.asList(testSale);
        when(saleDAO.getByProductId(1)).thenReturn(sales);
        
        List<Sale> result = salesService.getSalesByProduct(1);
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getProductId());
    }
    
    @Test
    public void testGetSalesByDateRange() {
        List<Sale> sales = Arrays.asList(testSale);
        when(saleDAO.getByDateRange("2024-01-01", "2024-12-31")).thenReturn(sales);
        
        List<Sale> result = salesService.getSalesByDateRange("2024-01-01", "2024-12-31");
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testGetMonthlyRevenue() {
        RevenueSummary summary = new RevenueSummary(2024, 1);
        summary.setTotalRevenue(new BigDecimal("1000.00"));
        summary.setTotalTransactions(10);
        summary.setTotalItemsSold(50);
        
        when(saleDAO.getMonthlyRevenue(2024, 1)).thenReturn(summary);
        
        RevenueSummary result = salesService.getMonthlyRevenue(2024, 1);
        
        assertNotNull(result);
        assertEquals(new BigDecimal("1000.00"), result.getTotalRevenue());
        assertEquals(10, result.getTotalTransactions());
    }
    
    @Test
    public void testGetYearlyRevenue() {
        RevenueSummary summary1 = new RevenueSummary(2024, 1);
        summary1.setTotalRevenue(new BigDecimal("500.00"));
        summary1.setTotalTransactions(5);
        summary1.setTotalItemsSold(25);
        
        RevenueSummary summary2 = new RevenueSummary(2024, 2);
        summary2.setTotalRevenue(new BigDecimal("500.00"));
        summary2.setTotalTransactions(5);
        summary2.setTotalItemsSold(25);
        
        List<RevenueSummary> summaries = Arrays.asList(summary1, summary2);
        when(saleDAO.getYearlyRevenue(2024)).thenReturn(summaries);
        
        List<RevenueSummary> result = salesService.getYearlyRevenue(2024);
        
        assertNotNull(result);
        assertEquals(2, result.size());
    }
    
    @Test
    public void testDeleteSale() {
        when(saleDAO.delete(1)).thenReturn(true);
        
        boolean result = salesService.deleteSale(1);
        
        assertTrue(result);
        verify(saleDAO, times(1)).delete(1);
    }
}
