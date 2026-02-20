package com.inventory.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Sale model class representing a sale transaction.
 */
public class Sale {
    private int id;
    private int productId;
    private String productName;
    private int quantitySold;
    private BigDecimal totalAmount;
    private LocalDateTime saleDate;

    // Default constructor
    public Sale() {
    }

    // Constructor with required fields
    public Sale(int productId, int quantitySold, BigDecimal totalAmount) {
        this.productId = productId;
        this.quantitySold = quantitySold;
        this.totalAmount = totalAmount;
    }

    // Full constructor
    public Sale(int id, int productId, String productName, int quantitySold, BigDecimal totalAmount, LocalDateTime saleDate) {
        this.id = id;
        this.productId = productId;
        this.productName = productName;
        this.quantitySold = quantitySold;
        this.totalAmount = totalAmount;
        this.saleDate = saleDate;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getQuantitySold() {
        return quantitySold;
    }

    public void setQuantitySold(int quantitySold) {
        this.quantitySold = quantitySold;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getSaleDate() {
        return saleDate;
    }

    public void setSaleDate(LocalDateTime saleDate) {
        this.saleDate = saleDate;
    }

    @Override
    public String toString() {
        return String.format(
            "Sale{id=%d, productName='%s', quantitySold=%d, totalAmount=%.2f, saleDate=%s}",
            id, productName, quantitySold, totalAmount, saleDate
        );
    }
}
