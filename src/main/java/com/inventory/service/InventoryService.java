package com.inventory.service;

import com.inventory.dao.ProductDAO;
import com.inventory.dao.ProductDAOImpl;
import com.inventory.exception.ProductNotFoundException;
import com.inventory.model.Product;
import com.inventory.util.LoggerUtil;
import java.util.List;

/**
 * Service class for inventory operations.
 * Implements business logic for product management.
 */
public class InventoryService {
    
    private ProductDAO productDAO;
    private static final LoggerUtil logger = LoggerUtil.getLogger(InventoryService.class);
    
    public InventoryService() {
        this.productDAO = new ProductDAOImpl();
    }
    
    // Package-private constructor for testing
    InventoryService(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }
    
    /**
     * Add a new product to the inventory.
     * @param product the product to add
     * @return the added product
     */
    public Product addProduct(Product product) {
        logger.info("Adding new product: " + product.getName());
        Product created = productDAO.create(product);
        logger.info("Product created with ID: " + created.getId());
        return created;
    }
    
    /**
     * Get a product by ID.
     * @param id the product ID
     * @return the product
     * @throws ProductNotFoundException if product not found
     */
    public Product getProduct(int id) {
        logger.debug("Fetching product with ID: " + id);
        Product product = productDAO.getById(id);
        if (product == null) {
            logger.warn("Product not found with ID: " + id);
            throw new ProductNotFoundException("Product not found with ID: " + id, id);
        }
        return product;
    }
    
    /**
     * Get all products.
     * @return list of all products
     */
    public List<Product> getAllProducts() {
        logger.debug("Fetching all products");
        return productDAO.getAll();
    }
    
    /**
     * Get products by category.
     * @param category the category
     * @return list of products in that category
     */
    public List<Product> getProductsByCategory(String category) {
        logger.debug("Fetching products in category: " + category);
        return productDAO.getByCategory(category);
    }
    
    /**
     * Get all low stock products.
     * @return list of low stock products
     */
    public List<Product> getLowStockProducts() {
        logger.debug("Fetching low stock products");
        return productDAO.getLowStockProducts();
    }
    
    /**
     * Update a product.
     * @param product the product to update
     * @return true if successful
     * @throws ProductNotFoundException if product not found
     */
    public boolean updateProduct(Product product) {
        logger.info("Updating product with ID: " + product.getId());
        // Verify product exists
        getProduct(product.getId());
        boolean result = productDAO.update(product);
        logger.info("Product update result: " + result);
        return result;
    }
    
    /**
     * Update product quantity.
     * @param id the product ID
     * @param quantity the new quantity
     * @return true if successful
     * @throws ProductNotFoundException if product not found
     */
    public boolean updateQuantity(int id, int quantity) {
        logger.info("Updating quantity for product ID: " + id + " to: " + quantity);
        // Verify product exists
        getProduct(id);
        boolean result = productDAO.updateQuantity(id, quantity);
        logger.info("Quantity update result: " + result);
        return result;
    }
    
    /**
     * Delete a product.
     * @param id the product ID
     * @return true if successful
     * @throws ProductNotFoundException if product not found
     */
    public boolean deleteProduct(int id) {
        logger.info("Deleting product with ID: " + id);
        // Verify product exists
        getProduct(id);
        boolean result = productDAO.delete(id);
        logger.info("Product deletion result: " + result);
        return result;
    }
    
    /**
     * Search products by name.
     * @param name the search term
     * @return list of matching products
     */
    public List<Product> searchProducts(String name) {
        logger.debug("Searching products with name: " + name);
        return productDAO.searchByName(name);
    }
    
    /**
     * Check and display low stock alerts.
     * @return list of low stock products
     */
    public List<Product> checkLowStockAlerts() {
        logger.info("Checking low stock alerts");
        List<Product> lowStockProducts = productDAO.getLowStockProducts();
        
        if (!lowStockProducts.isEmpty()) {
            System.out.println("\n========== LOW STOCK ALERTS ==========");
            for (Product product : lowStockProducts) {
                System.out.printf("WARNING: %s (ID: %d) - Only %d left (threshold: %d)%n",
                    product.getName(),
                    product.getId(),
                    product.getQuantity(),
                    product.getLowStockThreshold());
                logger.warn("Low stock alert: " + product.getName() + " (ID: " + product.getId() + 
                    ") - Quantity: " + product.getQuantity() + ", Threshold: " + product.getLowStockThreshold());
            }
            System.out.println("=======================================\n");
        }
        
        return lowStockProducts;
    }
    
    /**
     * Display all products in a formatted table.
     */
    public void displayAllProducts() {
        List<Product> products = getAllProducts();
        
        if (products.isEmpty()) {
            System.out.println("No products in inventory.");
            logger.info("No products found in inventory");
            return;
        }
        
        logger.info("Displaying " + products.size() + " products");
        System.out.println("\n========== INVENTORY LIST ==========");
        System.out.printf("%-5s %-20s %-15s %-10s %-10s %-10s%n",
            "ID", "Name", "Category", "Price", "Qty", "Status");
        System.out.println("-----------------------------------------------------------------------------------------");
        
        for (Product product : products) {
            String status = product.isOutOfStock() ? "OUT OF STOCK" : 
                           product.isLowStock() ? "LOW STOCK" : "In Stock";
            
            System.out.printf("%-5d %-20s %-15s Rs.%-9.2f %-10d %-10s%n",
                product.getId(),
                product.getName(),
                product.getCategory(),
                product.getPrice(),
                product.getQuantity(),
                status);
        }
        System.out.println("=================================================================\n");
    }
}
