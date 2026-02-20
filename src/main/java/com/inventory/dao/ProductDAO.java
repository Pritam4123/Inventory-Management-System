package com.inventory.dao;

import com.inventory.model.Product;
import java.util.List;

/**
 * Data Access Object interface for Product operations.
 */
public interface ProductDAO {
    
    /**
     * Create a new product in the database.
     * @param product the product to create
     * @return the created product with ID assigned
     */
    Product create(Product product);
    
    /**
     * Get a product by ID.
     * @param id the product ID
     * @return the product or null if not found
     */
    Product getById(int id);
    
    /**
     * Get all products.
     * @return list of all products
     */
    List<Product> getAll();
    
    /**
     * Get products by category.
     * @param category the category
     * @return list of products in that category
     */
    List<Product> getByCategory(String category);
    
    /**
     * Get products with low stock (quantity <= threshold).
     * @return list of low stock products
     */
    List<Product> getLowStockProducts();
    
    /**
     * Update an existing product.
     * @param product the product to update
     * @return true if update was successful
     */
    boolean update(Product product);
    
    /**
     * Update product quantity.
     * @param id the product ID
     * @param quantity the new quantity
     * @return true if update was successful
     */
    boolean updateQuantity(int id, int quantity);
    
    /**
     * Delete a product by ID.
     * @param id the product ID
     * @return true if deletion was successful
     */
    boolean delete(int id);
    
    /**
     * Search products by name.
     * @param name the search term
     * @return list of matching products
     */
    List<Product> searchByName(String name);
}
