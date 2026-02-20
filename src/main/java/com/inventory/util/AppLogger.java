package com.inventory.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * Logging utility class using Java's built-in logging framework.
 */
public class AppLogger {
    
    private static Logger logger;
    
    static {
        try {
            InputStream config = AppLogger.class.getClassLoader()
                .getResourceAsStream("logging.properties");
            if (config != null) {
                LogManager.getLogManager().readConfiguration(config);
            }
        } catch (IOException e) {
            System.err.println("Could not load logging configuration: " + e.getMessage());
        }
        logger = Logger.getLogger(AppLogger.class.getName());
    }
    
    /**
     * Get logger for a class.
     * @param clazz the class to get logger for
     * @return Logger instance
     */
    public static Logger getLogger(Class<?> clazz) {
        return Logger.getLogger(clazz.getName());
    }
    
    /**
     * Log info message.
     */
    public static void info(Class<?> clazz, String message) {
        getLogger(clazz).info(message);
    }
    
    /**
     * Log warning message.
     */
    public static void warning(Class<?> clazz, String message) {
        getLogger(clazz).warning(message);
    }
    
    /**
     * Log severe/error message.
     */
    public static void severe(Class<?> clazz, String message) {
        getLogger(clazz).severe(message);
    }
    
    /**
     * Log debug message.
     */
    public static void fine(Class<?> clazz, String message) {
        getLogger(clazz).fine(message);
    }
}
