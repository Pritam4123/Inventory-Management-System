package com.inventory.dao;

import com.inventory.exception.DatabaseException;
import com.inventory.model.Product;
import com.inventory.util.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of ProductDAO using JDBC.
 */
public class ProductDAOImpl implements ProductDAO {

    @Override
    public Product create(Product product) {
        String sql = "INSERT INTO products (name, description, category, price, quantity, low_stock_threshold) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setString(3, product.getCategory());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getQuantity());
            pstmt.setInt(6, product.getLowStockThreshold());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        product.setId(generatedKeys.getInt(1));
                    }
                }
            }
            return product;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error creating product: " + e.getMessage(), e);
        }
    }

    @Override
    public Product getById(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractProductFromResultSet(rs);
                }
            }
            return null;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting product by ID: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getAll() {
        String sql = "SELECT * FROM products ORDER BY name";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
            return products;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting all products: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getByCategory(String category) {
        String sql = "SELECT * FROM products WHERE category = ? ORDER BY name";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, category);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProductFromResultSet(rs));
                }
            }
            return products;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting products by category: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> getLowStockProducts() {
        String sql = "SELECT * FROM products WHERE quantity <= low_stock_threshold ORDER BY quantity";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                products.add(extractProductFromResultSet(rs));
            }
            return products;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error getting low stock products: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean update(Product product) {
        String sql = "UPDATE products SET name = ?, description = ?, category = ?, price = ?, quantity = ?, low_stock_threshold = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, product.getName());
            pstmt.setString(2, product.getDescription());
            pstmt.setString(3, product.getCategory());
            pstmt.setBigDecimal(4, product.getPrice());
            pstmt.setInt(5, product.getQuantity());
            pstmt.setInt(6, product.getLowStockThreshold());
            pstmt.setInt(7, product.getId());
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating product: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean updateQuantity(int id, int quantity) {
        String sql = "UPDATE products SET quantity = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, id);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error updating product quantity: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean delete(int id) {
        String sql = "DELETE FROM products WHERE id = ?";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, id);
            
            return pstmt.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error deleting product: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Product> searchByName(String name) {
        String sql = "SELECT * FROM products WHERE name ILIKE ? ORDER BY name";
        List<Product> products = new ArrayList<>();
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, "%" + name + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    products.add(extractProductFromResultSet(rs));
                }
            }
            return products;
            
        } catch (SQLException e) {
            throw new DatabaseException("Error searching products: " + e.getMessage(), e);
        }
    }

    /**
     * Extract product from ResultSet.
     */
    private Product extractProductFromResultSet(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setCategory(rs.getString("category"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setQuantity(rs.getInt("quantity"));
        product.setLowStockThreshold(rs.getInt("low_stock_threshold"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            product.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) {
            product.setUpdatedAt(updatedAt.toLocalDateTime());
        }
        
        return product;
    }
}
