package com.inventory.service;

import com.inventory.dao.ProductDAO;
import com.inventory.exception.ProductNotFoundException;
import com.inventory.model.Product;
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
import static org.mockito.Mockito.*;

/**
 * Unit tests for InventoryService.
 */
@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
    
    @Mock
    private ProductDAO productDAO;
    
    private InventoryService inventoryService;
    
    private Product testProduct;
    
    @BeforeEach
    public void setUp() {
        // Use the package-private constructor for testing
        inventoryService = new InventoryService(productDAO);
        
        testProduct = new Product();
        testProduct.setId(1);
        testProduct.setName("Test Product");
        testProduct.setDescription("Test Description");
        testProduct.setCategory("Electronics");
        testProduct.setPrice(new BigDecimal("99.99"));
        testProduct.setQuantity(50);
        testProduct.setLowStockThreshold(10);
    }
    
    @Test
    public void testAddProduct() {
        when(productDAO.create(any(Product.class))).thenReturn(testProduct);
        
        Product result = inventoryService.addProduct(testProduct);
        
        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        verify(productDAO, times(1)).create(any(Product.class));
    }
    
    @Test
    public void testGetProduct_Success() {
        when(productDAO.getById(1)).thenReturn(testProduct);
        
        Product result = inventoryService.getProduct(1);
        
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Test Product", result.getName());
    }
    
    @Test
    public void testGetProduct_NotFound() {
        when(productDAO.getById(999)).thenReturn(null);
        
        assertThrows(ProductNotFoundException.class, () -> {
            inventoryService.getProduct(999);
        });
    }
    
    @Test
    public void testGetAllProducts() {
        List<Product> products = Arrays.asList(testProduct);
        when(productDAO.getAll()).thenReturn(products);
        
        List<Product> result = inventoryService.getAllProducts();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
    }
    
    @Test
    public void testGetProductsByCategory() {
        List<Product> products = Arrays.asList(testProduct);
        when(productDAO.getByCategory("Electronics")).thenReturn(products);
        
        List<Product> result = inventoryService.getProductsByCategory("Electronics");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Electronics", result.get(0).getCategory());
    }
    
    @Test
    public void testGetLowStockProducts() {
        testProduct.setQuantity(5);
        List<Product> products = Arrays.asList(testProduct);
        when(productDAO.getLowStockProducts()).thenReturn(products);
        
        List<Product> result = inventoryService.getLowStockProducts();
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isLowStock());
    }
    
    @Test
    public void testUpdateProduct_Success() {
        when(productDAO.getById(1)).thenReturn(testProduct);
        when(productDAO.update(any(Product.class))).thenReturn(true);
        
        testProduct.setName("Updated Product");
        boolean result = inventoryService.updateProduct(testProduct);
        
        assertTrue(result);
        verify(productDAO, times(1)).update(any(Product.class));
    }
    
    @Test
    public void testUpdateProduct_NotFound() {
        when(productDAO.getById(999)).thenReturn(null);
        
        testProduct.setId(999);
        assertThrows(ProductNotFoundException.class, () -> {
            inventoryService.updateProduct(testProduct);
        });
    }
    
    @Test
    public void testDeleteProduct_Success() {
        when(productDAO.getById(1)).thenReturn(testProduct);
        when(productDAO.delete(1)).thenReturn(true);
        
        boolean result = inventoryService.deleteProduct(1);
        
        assertTrue(result);
        verify(productDAO, times(1)).delete(1);
    }
    
    @Test
    public void testSearchProducts() {
        List<Product> products = Arrays.asList(testProduct);
        when(productDAO.searchByName("Test")).thenReturn(products);
        
        List<Product> result = inventoryService.searchProducts("Test");
        
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Product", result.get(0).getName());
    }
}
