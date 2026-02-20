package com.inventory.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Application configuration manager.
 * Loads configuration from properties files and provides centralized access.
 */
public class ApplicationConfig {
    
    private static ApplicationConfig instance;
    private final Properties dbProperties;
    private final Properties appProperties;
    
    private ApplicationConfig() {
        dbProperties = new Properties();
        appProperties = new Properties();
        loadProperties();
    }
    
    /**
     * Get singleton instance of ApplicationConfig.
     */
    public static synchronized ApplicationConfig getInstance() {
        if (instance == null) {
            instance = new ApplicationConfig();
        }
        return instance;
    }
    
    /**
     * Load properties from files.
     */
    private void loadProperties() {
        // Load database properties
        try (InputStream dbInput = getClass().getClassLoader().getResourceAsStream("db.properties")) {
            if (dbInput != null) {
                dbProperties.load(dbInput);
            } else {
                // Fallback to file-based loading
                loadPropertiesFromFile("resources/db.properties", dbProperties);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load db.properties: " + e.getMessage());
        }
        
        // Load application properties
        try (InputStream appInput = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (appInput != null) {
                appProperties.load(appInput);
            }
        } catch (IOException e) {
            System.err.println("Warning: Could not load application.properties: " + e.getMessage());
        }
    }
    
    /**
     * Load properties from file path.
     */
    private void loadPropertiesFromFile(String filePath, Properties properties) {
        try (InputStream input = new java.io.FileInputStream(filePath)) {
            properties.load(input);
        } catch (IOException e) {
            System.err.println("Warning: Could not load properties from " + filePath + ": " + e.getMessage());
        }
    }
    
    /**
     * Get database property value.
     */
    public String getDbProperty(String key) {
        return dbProperties.getProperty(key);
    }
    
    /**
     * Get database property value with default.
     */
    public String getDbProperty(String key, String defaultValue) {
        return dbProperties.getProperty(key, defaultValue);
    }
    
    /**
     * Get application property value.
     */
    public String getAppProperty(String key) {
        return appProperties.getProperty(key);
    }
    
    /**
     * Get application property value with default.
     */
    public String getAppProperty(String key, String defaultValue) {
        return appProperties.getProperty(key, defaultValue);
    }
    
    /**
     * Get database URL.
     */
    public String getDatabaseUrl() {
        return getDbProperty("db.url", "jdbc:postgresql://localhost:5432/inventory");
    }
    
    /**
     * Get database username.
     */
    public String getDatabaseUsername() {
        return getDbProperty("db.username", "postgres");
    }
    
    /**
     * Get database password.
     */
    public String getDatabasePassword() {
        return getDbProperty("db.password", "password");
    }
    
    /**
     * Get database driver class name.
     */
    public String getDatabaseDriver() {
        return getDbProperty("db.driver", "org.postgresql.Driver");
    }
    
    /**
     * Get application name.
     */
    public String getApplicationName() {
        return getAppProperty("app.name", "Inventory Management System");
    }
    
    /**
     * Get application version.
     */
    public String getApplicationVersion() {
        return getAppProperty("app.version", "1.0.0");
    }
    
    /**
     * Reload configuration.
     */
    public synchronized void reload() {
        dbProperties.clear();
        appProperties.clear();
        loadProperties();
    }
}
