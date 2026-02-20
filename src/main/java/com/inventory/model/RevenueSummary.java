package com.inventory.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * RevenueSummary model class for monthly revenue reports.
 */
public class RevenueSummary {
    private int year;
    private int month;
    private String monthName;
    private int totalTransactions;
    private BigDecimal totalRevenue;
    private int totalItemsSold;

    // Default constructor
    public RevenueSummary() {
    }

    // Constructor with fields
    public RevenueSummary(int year, int month, String monthName, int totalTransactions, BigDecimal totalRevenue, int totalItemsSold) {
        this.year = year;
        this.month = month;
        this.monthName = monthName;
        this.totalTransactions = totalTransactions;
        this.totalRevenue = totalRevenue;
        this.totalItemsSold = totalItemsSold;
    }

    // Getters and Setters
    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public String getMonthName() {
        return monthName;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public int getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(int totalTransactions) {
        this.totalTransactions = totalTransactions;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }

    public int getTotalItemsSold() {
        return totalItemsSold;
    }

    public void setTotalItemsSold(int totalItemsSold) {
        this.totalItemsSold = totalItemsSold;
    }

    @Override
    public String toString() {
        return String.format(
            "RevenueSummary{year=%d, month=%d (%s), transactions=%d, revenue=%.2f, itemsSold=%d}",
            year, month, monthName, totalTransactions, totalRevenue, totalItemsSold
        );
    }

    /**
     * Get formatted summary for display.
     */
    public String getFormattedSummary() {
        return String.format(
            "%s %d: Revenue=$%.2f, Transactions=%d, Items Sold=%d",
            monthName, year, totalRevenue, totalTransactions, totalItemsSold
        );
    }
}
