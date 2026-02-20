package com.inventory.util;

import com.inventory.dto.ProductDTO;
import com.inventory.dto.SaleDTO;
import com.inventory.model.Product;
import com.inventory.model.Sale;
import java.time.LocalDateTime;

/**
 * Mapper class for converting between Entity and DTO objects.
 * Provides bidirectional mapping between domain models and data transfer objects.
 */
public class EntityMapper {
    
    private static final LoggerUtil logger = LoggerUtil.getLogger(EntityMapper.class);
    
    /**
     * Convert Product entity to ProductDTO.
     */
    public static ProductDTO toProductDTO(Product product) {
        if (product == null) {
            return null;
        }
        
        ProductDTO dto = new ProductDTO();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setLowStockThreshold(product.getLowStockThreshold());
        dto.setCreatedAt(product.getCreatedAt());
        dto.setUpdatedAt(product.getUpdatedAt());
        
        return dto;
    }
    
    /**
     * Convert ProductDTO to Product entity.
     */
    public static Product toProductEntity(ProductDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Product product = new Product();
        product.setId(dto.getId());
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setCategory(dto.getCategory());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        product.setLowStockThreshold(dto.getLowStockThreshold());
        
        // Set timestamps if provided
        if (dto.getCreatedAt() != null) {
            product.setCreatedAt(dto.getCreatedAt());
        }
        if (dto.getUpdatedAt() != null) {
            product.setUpdatedAt(dto.getUpdatedAt());
        }
        
        return product;
    }
    
    /**
     * Convert Sale entity to SaleDTO.
     */
    public static SaleDTO toSaleDTO(Sale sale) {
        if (sale == null) {
            return null;
        }
        
        SaleDTO dto = new SaleDTO();
        dto.setId(sale.getId());
        dto.setProductId(sale.getProductId());
        dto.setProductName(sale.getProductName());
        dto.setQuantity(sale.getQuantitySold());
        dto.setTotalAmount(sale.getTotalAmount());
        dto.setSaleDate(sale.getSaleDate());
        
        // Calculate unit price if we have both quantity and total amount
        if (sale.getQuantitySold() > 0 && sale.getTotalAmount() != null) {
            dto.setUnitPrice(sale.getTotalAmount().divide(java.math.BigDecimal.valueOf(sale.getQuantitySold())));
        }
        
        return dto;
    }
    
    /**
     * Convert SaleDTO to Sale entity.
     */
    public static Sale toSaleEntity(SaleDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Sale sale = new Sale();
        sale.setId(dto.getId() != null ? dto.getId() : 0);
        sale.setProductId(dto.getProductId() != null ? dto.getProductId() : 0);
        sale.setProductName(dto.getProductName());
        sale.setQuantitySold(dto.getQuantity() != null ? dto.getQuantity() : 0);
        
        // Calculate total amount if not provided
        if (dto.getTotalAmount() != null) {
            sale.setTotalAmount(dto.getTotalAmount());
        } else if (dto.getUnitPrice() != null && dto.getQuantity() != null) {
            sale.setTotalAmount(dto.getUnitPrice().multiply(java.math.BigDecimal.valueOf(dto.getQuantity())));
        } else {
            sale.setTotalAmount(java.math.BigDecimal.ZERO);
        }
        
        sale.setSaleDate(dto.getSaleDate() != null ? dto.getSaleDate() : LocalDateTime.now());
        
        return sale;
    }
    
    /**
     * Update Product entity from ProductDTO.
     */
    public static void updateProductFromDTO(ProductDTO dto, Product product) {
        if (dto == null || product == null) {
            return;
        }
        
        if (dto.getName() != null) {
            product.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            product.setDescription(dto.getDescription());
        }
        if (dto.getCategory() != null) {
            product.setCategory(dto.getCategory());
        }
        if (dto.getPrice() != null) {
            product.setPrice(dto.getPrice());
        }
        if (dto.getQuantity() != null) {
            product.setQuantity(dto.getQuantity());
        }
        if (dto.getLowStockThreshold() != null) {
            product.setLowStockThreshold(dto.getLowStockThreshold());
        }
        
        product.setUpdatedAt(LocalDateTime.now());
    }
    
    /**
     * Update Sale entity from SaleDTO.
     */
    public static void updateSaleFromDTO(SaleDTO dto, Sale sale) {
        if (dto == null || sale == null) {
            return;
        }
        
        if (dto.getProductId() != null) {
            sale.setProductId(dto.getProductId());
        }
        if (dto.getProductName() != null) {
            sale.setProductName(dto.getProductName());
        }
        if (dto.getQuantity() != null) {
            sale.setQuantitySold(dto.getQuantity());
        }
        if (dto.getTotalAmount() != null) {
            sale.setTotalAmount(dto.getTotalAmount());
        }
    }
    
    /**
     * Validate and convert ProductDTO to Product entity.
     * Returns null if validation fails.
     */
    public static Product toValidatedProductEntity(ProductDTO dto) {
        if (dto == null) {
            logger.warn("Cannot convert null ProductDTO to entity");
            return null;
        }
        
        // Validate required fields
        if (dto.getName() == null || dto.getName().trim().isEmpty()) {
            logger.warn("Product name is required");
            return null;
        }
        if (dto.getPrice() == null || dto.getPrice().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            logger.warn("Product price must be positive");
            return null;
        }
        
        return toProductEntity(dto);
    }
    
    /**
     * Validate and convert SaleDTO to Sale entity.
     * Returns null if validation fails.
     */
    public static Sale toValidatedSaleEntity(SaleDTO dto) {
        if (dto == null) {
            logger.warn("Cannot convert null SaleDTO to entity");
            return null;
        }
        
        // Validate required fields
        if (dto.getProductId() == null || dto.getProductId() <= 0) {
            logger.warn("Valid product ID is required");
            return null;
        }
        if (dto.getQuantity() == null || dto.getQuantity() <= 0) {
            logger.warn("Sale quantity must be positive");
            return null;
        }
        
        return toSaleEntity(dto);
    }
}
