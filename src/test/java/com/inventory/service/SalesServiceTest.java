package com.inventory.service;

import com.inventory.dao.ProductDAO;
import com.inventory.dao.ProductDAOImpl;
import com.inventory.dao.SaleDAO;
import com.inventory.dao.SaleDAOImpl;
import com.inventory.exception.InsufficientStockException;
import com.inventory.exception.ProductNotFoundException;
import com.inventory.model.Product;
import com.inventory.model.Sale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SalesService.
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
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setQuantity(50);
        testProduct.setLowStockThreshold(10);
        
        testSale = new Sale();
        testSale.setId(1);
        testSale.setProductId(1);
        testSale.setQuantity(5);
        testSale.setTotalAmount(new BigDecimal("499.95"));
        testSale.setSaleDate(LocalDateTime.now());
    }
    
    @Test
    public void testProcessSale_Success() {
        when(productDAO.getById(1)).thenReturn(testProduct);
        when(productDAO.updateQuantity(1, 45)).thenReturn(true);
        when(saleDAO.create(any(Sale.class))).thenReturn(testSale);
        
        Sale result = salesService.processSale(1, 5);
        
        assertNotNull(result);
        assertEquals(1, result.getProductId());
        assertEquals(5, result.getQuantity());
        verify(productDAO, times(1)).updateQuantity(1, 45);
        verify(saleDAO, times(1)).create(any(Sale.class));
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
        testProduct.setQuantity(3);
        when(productDAO.getById(1)).thenReturn(testProduct);
        
        assertThrows(InsufficientStockException.class, () -> {
            salesService.processSale(1, 5);
        });
    }
    
    @Test
    public void testGetAllSales() {
        List<Sale> sales = Arrays.asList(testSale);
        when(saleDAO.getAll()).thenReturn(sales);
        
        List<Sale> result = salesService.getAllSales();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getProductId());
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
        when(saleDAO.getByDateRange(any(), any())).thenReturn(sales);
        
        List<Sale> result = salesService.getSalesByDateRange("2024-01-01", "2024-12-31");
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }
    
    @Test
    public void testGetMonthlyRevenue() {
        when(saleDAO.getMonthlyRevenue(2024, 1)).thenReturn(new BigDecimal("5000.00"));
        
        BigDecimal result = salesService.getMonthlyRevenue(2024, 1);
        
        assertNotNull(result);
        assertEquals(new BigDecimal("5000.00"), result);
    }
    
    @Test
    public void testGetYearlyRevenue() {
        when(saleDAO.getYearlyRevenue(2024)).thenReturn(new BigDecimal("60000.00"));
        
        BigDecimal result = salesService.getYearlyRevenue(2024);
        
        assertNotNull(result);
        assertEquals(new BigDecimal("60000.00"), result);
    }
}
