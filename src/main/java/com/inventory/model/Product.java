package com.inventory.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Product model class representing a product in the inventory.
 */
public class Product {
    private int id;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private int quantity;
    private int lowStockThreshold;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Default constructor
    public Product() {
    }

    // Constructor with required fields
    public Product(String name, String description, String category, BigDecimal price, int quantity, int lowStockThreshold) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.lowStockThreshold = lowStockThreshold;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getLowStockThreshold() {
        return lowStockThreshold;
    }

    public void setLowStockThreshold(int lowStockThreshold) {
        this.lowStockThreshold = lowStockThreshold;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    /**
     * Check if the product is low on stock.
     * @return true if quantity is less than or equal to lowStockThreshold
     */
    public boolean isLowStock() {
        return quantity <= lowStockThreshold;
    }

    /**
     * Check if the product is out of stock.
     * @return true if quantity is 0
     */
    public boolean isOutOfStock() {
        return quantity <= 0;
    }

    @Override
    public String toString() {
        return String.format(
            "Product{id=%d, name='%s', category='%s', price=%.2f, quantity=%d, lowStockThreshold=%d}",
            id, name, category, price, quantity, lowStockThreshold
        );
    }
}
