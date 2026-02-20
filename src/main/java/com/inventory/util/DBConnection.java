package com.inventory.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Database connection utility class.
 * Handles database connection using environment variables or config file.
 * Priority: Environment Variables > Config File > Default Values
 */
public class DBConnection {
    private static String url;
    private static String username;
    private static String password;
    private static String driver;
    
    static {
        loadProperties();
    }
    
    /**
     * Load database properties.
     * Priority: Environment Variables > Config File > Default Values
     */
    private static void loadProperties() {
        // First, try to get from environment variables
        url = System.getenv("DB_URL");
        username = System.getenv("DB_USERNAME");
        password = System.getenv("DB_PASSWORD");
        driver = System.getenv("DB_DRIVER");
        
        // If environment variables are set, use them
        if (url != null && username != null && password != null) {
            System.out.println("Using database configuration from environment variables.");
            loadDriver();
            return;
        }
        
        // Fall back to config file
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("resources/db.properties")) {
            props.load(fis);
            
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            driver = props.getProperty("db.driver");
            
            System.out.println("Using database configuration from config file.");
            loadDriver();
            
        } catch (IOException e) {
            System.err.println("Error loading database properties file: " + e.getMessage());
            setDefaultValues();
        }
    }
    
    /**
     * Load the database driver.
     */
    private static void loadDriver() {
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load database driver: " + driver, e);
        }
    }
    
    /**
     * Set default values (for development only).
     */
    private static void setDefaultValues() {
        System.err.println("WARNING: Using default database configuration. Not recommended for production!");
        url = "jdbc:postgresql://localhost:5432/inventory_db";
        username = "postgres";
        password = "postgres123";
        driver = "org.postgresql.Driver";
        loadDriver();
    }
    
    /**
     * Get a database connection.
     * @return Connection object
     * @throws SQLException if connection fails
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
    
    /**
     * Test the database connection.
     * @return true if connection is successful
     */
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Get the database URL.
     * @return the database URL
     */
    public static String getUrl() {
        return url;
    }
    
    /**
     * Get database username.
     * @return the database username
     */
    public static String getUsername() {
        return username;
    }
}
