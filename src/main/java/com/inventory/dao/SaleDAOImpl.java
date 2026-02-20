package com.inventory.dao;

import com.inventory.exception.DatabaseException;
import com.inventory.model.RevenueSummary;
import com.inventory.model.Sale;
import com.inventory.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of SaleDAO using JDBC.
 */
public class SaleDAOImpl implements SaleDAO {

    @Override
    public Sale create(Sale sale) {
        String sql = "INSERT INTO sales (product_id, quantity_sold, total_amount, sale_date) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, sale.getProductId());
            pstmt.setInt(2, sale.getQuantitySold());
            pstmt.setBigDecimal(3, sale.getTotalAmount());
            
            if (sale.getSaleDate() != null) {
                pstmt.setTimestamp(4, Timestamp.valueOf(sale.getSaleDate()));
            } else {
                pstmt.setTimestamp(4, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        sale.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return sale;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error creating sale: " + e.getMessage(), e);
        }
    }

    @Override
    public Sale getById(int id) {
        String sql = "SELECT s.id, s.product_id, p.name as product_name, s.quantity_sold, s.total_amount, s.sale_date " +
                     "FROM sales s JOIN products p ON s.product_id = p.id WHERE s.id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractSaleFromResultSet(rs);
                }
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting sale by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Sale> getAll() {
        String sql = "SELECT s.id, s.product_id, p.name as product_name, s.quantity_sold, s.total_amount, s.sale_date " +
                     "FROM sales s JOIN products p ON s.product_id = p.id ORDER BY s.sale_date DESC";
        List<Sale> sales = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                sales.add(extractSaleFromResultSet(rs));
            }
            return sales;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all sales: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Sale> getByProductId(int productId) {
        String sql = "SELECT s.id, s.product_id, p.name as product_name, s.quantity_sold, s.total_amount, s.sale_date " +
                     "FROM sales s JOIN products p ON s.product_id = p.id WHERE s.product_id = ? ORDER BY s.sale_date DESC";
        List<Sale> sales = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, productId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sales.add(extractSaleFromResultSet(rs));
                }
            }
            return sales;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting sales by product ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Sale> getByDateRange(String startDate, String endDate) {
        String sql = "SELECT s.id, s.product_id, p.name as product_name, s.quantity_sold, s.total_amount, s.sale_date " +
                     "FROM sales s JOIN products p ON s.product_id = p.id " +
                     "WHERE s.sale_date BETWEEN ? AND ? ORDER BY s.sale_date DESC";
        List<Sale> sales = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, startDate);
            pstmt.setString(2, endDate);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    sales.add(extractSaleFromResultSet(rs));
                }
            }
            return sales;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting sales by date range: " + e.getMessage(), e);
        }
    }

    @Override
    public RevenueSummary getMonthlyRevenue(int year, int month) {
        String sql = "SELECT COUNT(*) as total_transactions, " +
                     "COALESCE(SUM(total_amount), 0) as total_revenue, " +
                     "COALESCE(SUM(quantity_sold), 0) as total_items_sold " +
                     "FROM sales " +
                     "WHERE EXTRACT(YEAR FROM sale_date) = ? AND EXTRACT(MONTH FROM sale_date) = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, year);
            pstmt.setInt(2, month);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String monthName = java.time.Month.of(month).name();
                    return new RevenueSummary(
                        year,
                        month,
                        monthName,
                        rs.getInt("total_transactions"),
                        rs.getBigDecimal("total_revenue"),
                        rs.getInt("total_items_sold")
                    );
                }
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting monthly revenue: " + e.getMessage(), e);
        }
    }

    @Override
    public List<RevenueSummary> getYearlyRevenue(int year) {
        String sql = "SELECT EXTRACT(MONTH FROM sale_date) as month, " +
                     "COUNT(*) as total_transactions, " +
                     "COALESCE(SUM(total_amount), 0) as total_revenue, " +
                     "COALESCE(SUM(quantity_sold), 0) as total_items_sold " +
                     "FROM sales " +
                     "WHERE EXTRACT(YEAR FROM sale_date) = ? " +
                     "GROUP BY EXTRACT(MONTH FROM sale_date) " +
                     "ORDER BY month";
        List<RevenueSummary> summaries = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, year);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int month = rs.getInt("month");
                    String monthName = java.time.Month.of(month).name();
                    summaries.add(new RevenueSummary(
                        year,
                        month,
                        monthName,
                        rs.getInt("total_transactions"),
                        rs.getBigDecimal("total_revenue"),
                        rs.getInt("total_items_sold")
                    ));
                }
            }
            return summaries;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting yearly revenue: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM sales WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting sale: " + e.getMessage(), e);
        }
    }

    /**
     * Extract sale from ResultSet.
     */
    private Sale extractSaleFromResultSet(ResultSet rs) throws SQLException {
        Sale sale = new Sale();
        sale.setId(rs.getInt("id"));
        sale.setProductId(rs.getInt("product_id"));
        sale.setProductName(rs.getString("product_name"));
        sale.setQuantitySold(rs.getInt("quantity_sold"));
        sale.setTotalAmount(rs.getBigDecimal("total_amount"));
        
        Timestamp saleDate = rs.getTimestamp("sale_date");
        if (saleDate != null) {
            sale.setSaleDate(saleDate.toLocalDateTime());
        }
        
        return sale;
    }
}
