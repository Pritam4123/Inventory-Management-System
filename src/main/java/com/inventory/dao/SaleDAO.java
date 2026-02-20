package com.inventory.dao;

import com.inventory.model.RevenueSummary;
import com.inventory.model.Sale;
import java.util.List;

/**
 * Data Access Object interface for Sale operations.
 */
public interface SaleDAO {
    
    /**
     * Create a new sale in the database.
     * @param sale the sale to create
     * @return the created sale with ID assigned
     */
    Sale create(Sale sale);
    
    /**
     * Get a sale by ID.
     * @param id the sale ID
     * @return the sale or null if not found
     */
    Sale getById(int id);
    
    /**
     * Get all sales.
     * @return list of all sales
     */
    List<Sale> getAll();
    
    /**
     * Get sales by product ID.
     * @param productId the product ID
     * @return list of sales for that product
     */
    List<Sale> getByProductId(int productId);
    
    /**
     * Get sales by date range.
     * @param startDate start date
     * @param endDate end date
     * @return list of sales in that date range
     */
    List<Sale> getByDateRange(String startDate, String endDate);
    
    /**
     * Get monthly revenue summary.
     * @param year the year
     * @param month the month (1-12)
     * @return revenue summary for the month
     */
    RevenueSummary getMonthlyRevenue(int year, int month);
    
    /**
     * Get yearly revenue summary.
     * @param year the year
     * @return revenue summary for the year
     */
    List<RevenueSummary> getYearlyRevenue(int year);
    
    /**
     * Delete a sale by ID.
     * @param id the sale ID
     * @return true if deletion was successful
     */
    boolean delete(int id);
}
