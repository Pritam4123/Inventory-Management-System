package com.inventory.service;

import com.inventory.dao.ProductDAO;
import com.inventory.dao.ProductDAOImpl;
import com.inventory.dao.SaleDAO;
import com.inventory.dao.SaleDAOImpl;
import com.inventory.exception.InsufficientStockException;
import com.inventory.exception.ProductNotFoundException;
import com.inventory.model.Product;
import com.inventory.model.RevenueSummary;
import com.inventory.model.Sale;
import com.inventory.util.DBConnection;
import com.inventory.util.LoggerUtil;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;

/**
 * Service class for sales operations.
 * Implements business logic for sales management with transaction support.
 */
public class SalesService {
    
    private SaleDAO saleDAO;
    private ProductDAO productDAO;
    private static final LoggerUtil logger = LoggerUtil.getLogger(SalesService.class);
    
    public SalesService() {
        this.saleDAO = new SaleDAOImpl();
        this.productDAO = new ProductDAOImpl();
    }
    
    /**
     * Process a sale with automatic stock deduction.
     * Uses transaction to ensure data consistency.
     * @param productId the product ID
     * @param quantity the quantity to sell
     * @return the created sale
     */
    public Sale processSale(int productId, int quantity) {
        logger.info("Processing sale - Product ID: " + productId + ", Quantity: " + quantity);
        Connection conn = null;
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            
            // Get product and check stock
            Product product = productDAO.getById(productId);
            if (product == null) {
                logger.warn("Product not found with ID: " + productId);
                throw new ProductNotFoundException("Product not found with ID: " + productId, productId);
            }
            
            if (product.getQuantity() < quantity) {
                logger.warn("Insufficient stock - Available: " + product.getQuantity() + ", Requested: " + quantity);
                throw new InsufficientStockException(
                    "Insufficient stock for product: " + product.getName(),
                    productId,
                    quantity,
                    product.getQuantity()
                );
            }
            
            // Calculate total amount
            BigDecimal totalAmount = product.getPrice().multiply(BigDecimal.valueOf(quantity));
            logger.debug("Calculated total amount: " + totalAmount);
            
            // Create sale record
            Sale sale = new Sale(productId, quantity, totalAmount);
            sale = saleDAO.create(sale);
            logger.debug("Sale record created with ID: " + sale.getId());
            
            // Update product quantity (deduct stock)
            int newQuantity = product.getQuantity() - quantity;
            productDAO.updateQuantity(productId, newQuantity);
            logger.debug("Product quantity updated from " + product.getQuantity() + " to " + newQuantity);
            
            // Commit transaction
            conn.commit();
            logger.info("Transaction committed successfully for sale ID: " + sale.getId());
            
            System.out.println("Sale processed successfully!");
            System.out.printf("Sold %d units of %s for Rs.%.2f%n", 
                quantity, product.getName(), totalAmount);
            
            return sale;
            
        } catch (InsufficientStockException | ProductNotFoundException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    logger.error("Transaction rolled back due to: " + e.getMessage());
                } catch (Exception ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            throw e;
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                    logger.error("Transaction rolled back due to: " + e.getMessage());
                } catch (Exception ex) {
                    System.err.println("Error rolling back transaction: " + ex.getMessage());
                }
            }
            logger.error("Error processing sale: " + e.getMessage(), e);
            throw new RuntimeException("Error processing sale: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                    logger.debug("Database connection closed");
                } catch (Exception e) {
                    System.err.println("Error closing connection: " + e.getMessage());
                }
            }
        }
    }
    
    /**
     * Get a sale by ID.
     * @param id the sale ID
     * @return the sale
     */
    public Sale getSale(int id) {
        return saleDAO.getById(id);
    }
    
    /**
     * Get all sales.
     * @return list of all sales
     */
    public List<Sale> getAllSales() {
        return saleDAO.getAll();
    }
    
    /**
     * Get sales for a specific product.
     * @param productId the product ID
     * @return list of sales for that product
     */
    public List<Sale> getSalesByProduct(int productId) {
        return saleDAO.getByProductId(productId);
    }
    
    /**
     * Get sales by date range.
     * @param startDate start date (yyyy-MM-dd)
     * @param endDate end date (yyyy-MM-dd)
     * @return list of sales in that range
     */
    public List<Sale> getSalesByDateRange(String startDate, String endDate) {
        return saleDAO.getByDateRange(startDate, endDate);
    }
    
    /**
     * Get monthly revenue summary.
     * @param year the year
     * @param month the month (1-12)
     * @return revenue summary
     */
    public RevenueSummary getMonthlyRevenue(int year, int month) {
        return saleDAO.getMonthlyRevenue(year, month);
    }
    
    /**
     * Get yearly revenue summary.
     * @param year the year
     * @return list of monthly revenue summaries
     */
    public List<RevenueSummary> getYearlyRevenue(int year) {
        return saleDAO.getYearlyRevenue(year);
    }
    
    /**
     * Delete a sale (for testing purposes).
     * @param id the sale ID
     * @return true if successful
     */
    public boolean deleteSale(int id) {
        return saleDAO.delete(id);
    }
    
    /**
     * Display all sales in a formatted table.
     */
    public void displayAllSales() {
        List<Sale> sales = getAllSales();
        
        if (sales.isEmpty()) {
            System.out.println("No sales records found.");
            return;
        }
        
        System.out.println("\n========== SALES HISTORY ==========");
        System.out.printf("%-5s %-20s %-10s %-12s %-20s%n",
            "ID", "Product", "Qty", "Amount", "Date");
        System.out.println("-----------------------------------------------------------------");
        
        for (Sale sale : sales) {
            System.out.printf("%-5d %-20s %-10d Rs.%-11.2f %-20s%n",
                sale.getId(),
                sale.getProductName(),
                sale.getQuantitySold(),
                sale.getTotalAmount(),
                sale.getSaleDate().toString());
        }
        System.out.println("=================================================================\n");
    }
    
    /**
     * Display monthly revenue summary.
     * @param year the year
     * @param month the month (1-12)
     */
    public void displayMonthlyRevenue(int year, int month) {
        RevenueSummary summary = getMonthlyRevenue(year, month);
        
        if (summary == null) {
            System.out.println("No revenue data found for the specified period.");
            return;
        }
        
        String monthName = java.time.Month.of(month).name();
        
        System.out.println("\n========== MONTHLY REVENUE ==========");
        System.out.printf("Period: %s %d%n", monthName, year);
        System.out.printf("Total Transactions: %d%n", summary.getTotalTransactions());
        System.out.printf("Total Revenue: Rs.%.2f%n", summary.getTotalRevenue());
        System.out.printf("Total Items Sold: %d%n", summary.getTotalItemsSold());
        System.out.println("=====================================\n");
    }
    
    /**
     * Display yearly revenue summary.
     * @param year the year
     */
    public void displayYearlyRevenue(int year) {
        List<RevenueSummary> summaries = getYearlyRevenue(year);
        
        if (summaries.isEmpty()) {
            System.out.println("No revenue data found for the specified year.");
            return;
        }
        
        BigDecimal totalRevenue = BigDecimal.ZERO;
        int totalTransactions = 0;
        int totalItemsSold = 0;
        
        System.out.println("\n========== YEARLY REVENUE ==========");
        System.out.printf("Year: %d%n%n", year);
        System.out.printf("%-10s %-15s %-15s %-15s%n", 
            "Month", "Transactions", "Items Sold", "Revenue");
        System.out.println("------------------------------------------------");
        
        for (RevenueSummary summary : summaries) {
            String monthName = java.time.Month.of(summary.getMonth()).name();
            System.out.printf("%-10s %-15d %-15d Rs.%-14.2f%n",
                monthName,
                summary.getTotalTransactions(),
                summary.getTotalItemsSold(),
                summary.getTotalRevenue());
            
            totalRevenue = totalRevenue.add(summary.getTotalRevenue());
            totalTransactions += summary.getTotalTransactions();
            totalItemsSold += summary.getTotalItemsSold();
        }
        
        System.out.println("------------------------------------------------");
        System.out.printf("TOTAL:      %-15d %-15d Rs.%-14.2f%n",
            totalTransactions, totalItemsSold, totalRevenue);
        System.out.println("=====================================\n");
    }
}
