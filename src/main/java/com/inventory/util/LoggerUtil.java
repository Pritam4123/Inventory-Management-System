package com.inventory.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Enhanced Logging Utility for the Inventory Management System.
 * Provides structured logging capabilities with different log levels.
 */
public class LoggerUtil {
    
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String YELLOW = "\033[0;33m";
    private static final String GREEN = "\033[0;32m";
    private static final String BLUE = "\033[0;34m";
    private static final String CYAN = "\033[0;36m";
    
    // Log levels
    public enum Level {
        DEBUG, INFO, WARN, ERROR
    }
    
    private final String className;
    private Level minLevel;
    
    // Singleton instance for global logging configuration
    private static Level globalMinLevel = Level.INFO;
    private static boolean enableColors = true;
    private static boolean enableTimestamps = true;
    
    private LoggerUtil(Class<?> clazz) {
        this.className = clazz.getSimpleName();
        this.minLevel = globalMinLevel;
    }
    
    /**
     * Create a logger for a class.
     */
    public static LoggerUtil getLogger(Class<?> clazz) {
        return new LoggerUtil(clazz);
    }
    
    /**
     * Set global minimum log level.
     */
    public static void setGlobalMinLevel(Level level) {
        globalMinLevel = level;
    }
    
    /**
     * Enable or disable colored output.
     */
    public static void setEnableColors(boolean enable) {
        enableColors = enable;
    }
    
    /**
     * Enable or disable timestamps.
     */
    public static void setEnableTimestamps(boolean enable) {
        enableTimestamps = enable;
    }
    
    /**
     * Log a debug message.
     */
    public void debug(String message) {
        log(Level.DEBUG, message);
    }
    
    /**
     * Log a debug message with parameters.
     */
    public void debug(String message, Object... params) {
        log(Level.DEBUG, String.format(message, params));
    }
    
    /**
     * Log an info message.
     */
    public void info(String message) {
        log(Level.INFO, message);
    }
    
    /**
     * Log an info message with parameters.
     */
    public void info(String message, Object... params) {
        log(Level.INFO, String.format(message, params));
    }
    
    /**
     * Log a warning message.
     */
    public void warn(String message) {
        log(Level.WARN, message);
    }
    
    /**
     * Log a warning message with parameters.
     */
    public void warn(String message, Object... params) {
        log(Level.WARN, String.format(message, params));
    }
    
    /**
     * Log an error message.
     */
    public void error(String message) {
        log(Level.ERROR, message);
    }
    
    /**
     * Log an error message with parameters.
     */
    public void error(String message, Object... params) {
        log(Level.ERROR, String.format(message, params));
    }
    
    /**
     * Log an error message with exception.
     */
    public void error(String message, Throwable throwable) {
        log(Level.ERROR, message + " - Exception: " + throwable.getMessage());
    }
    
    /**
     * Log a method entry.
     */
    public void logMethodEntry(String methodName, Object... params) {
        if (minLevel == Level.DEBUG) {
            StringBuilder sb = new StringBuilder("ENTER: ").append(methodName).append("(");
            for (int i = 0; i < params.length; i++) {
                if (i > 0) sb.append(", ");
                sb.append(params[i]);
            }
            sb.append(")");
            log(Level.DEBUG, sb.toString());
        }
    }
    
    /**
     * Log a method exit.
     */
    public void logMethodExit(String methodName, Object returnValue) {
        if (minLevel == Level.DEBUG) {
            log(Level.DEBUG, "EXIT: " + methodName + "() -> " + returnValue);
        }
    }
    
    /**
     * Core logging method.
     */
    private void log(Level level, String message) {
        if (level.ordinal() < minLevel.ordinal()) {
            return;
        }
        
        StringBuilder sb = new StringBuilder();
        
        // Add timestamp
        if (enableTimestamps) {
            sb.append("[").append(LocalDateTime.now().format(TIMESTAMP_FORMAT)).append("] ");
        }
        
        // Add log level
        sb.append("[").append(level.name()).append("] ");
        
        // Add class name
        sb.append("[").append(className).append("] ");
        
        // Add message
        sb.append(message);
        
        // Print with appropriate color
        String coloredMessage = sb.toString();
        if (enableColors) {
            switch (level) {
                case DEBUG:
                    System.out.println(CYAN + coloredMessage + RESET);
                    break;
                case INFO:
                    System.out.println(GREEN + coloredMessage + RESET);
                    break;
                case WARN:
                    System.out.println(YELLOW + coloredMessage + RESET);
                    break;
                case ERROR:
                    System.out.println(RED + coloredMessage + RESET);
                    break;
                default:
                    System.out.println(coloredMessage);
            }
        } else {
            System.out.println(coloredMessage);
        }
    }
    
    /**
     * Log a separator line.
     */
    public void logSeparator() {
        log(Level.INFO, "=====================================");
    }
    
    /**
     * Log a separator line with custom text.
     */
    public void logSeparator(String text) {
        log(Level.INFO, "=== " + text + " ===");
    }
}
