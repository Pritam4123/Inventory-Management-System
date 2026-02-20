package com.inventory;

import com.inventory.exception.InsufficientStockException;
import com.inventory.exception.ProductNotFoundException;
import com.inventory.model.Product;
import com.inventory.service.InventoryService;
import com.inventory.service.SalesService;
import com.inventory.util.DBConnection;
import com.inventory.util.InputHelper;
import java.math.BigDecimal;
import java.util.List;

/**
 * Main class for Inventory Management System.
 * Provides console-based UI for managing inventory and sales.
 */
public class Main {
    
    private static InventoryService inventoryService = new InventoryService();
    private static SalesService salesService = new SalesService();
    
    public static void main(String[] args) {
        // Test database connection
        System.out.println("Connecting to database...");
        if (!DBConnection.testConnection()) {
            System.err.println("Failed to connect to database. Please check your configuration.");
            System.exit(1);
        }
        System.out.println("Database connected successfully!\n");
        
        // Show low stock alerts on startup
        inventoryService.checkLowStockAlerts();
        
        // Main menu loop
        boolean running = true;
        while (running) {
            try {
                running = showMainMenu();
            } catch (Exception e) {
                System.err.println("Error: " + e.getMessage());
                InputHelper.pause();
            }
        }
        
        System.out.println("Thank you for using Inventory Management System. Goodbye!");
        InputHelper.close();
    }
    
    /**
     * Display main menu and handle user selection.
     */
    private static boolean showMainMenu() {
        InputHelper.clearScreen();
        System.out.println("=====================================");
        System.out.println("  INVENTORY MANAGEMENT SYSTEM");
        System.out.println("=====================================");
        System.out.println("1. Product Management");
        System.out.println("2. Sales Management");
        System.out.println("3. Reports & Revenue");
        System.out.println("4. Low Stock Alerts");
        System.out.println("5. Exit");
        System.out.println("=====================================");
        
        int choice = InputHelper.getIntInRange("Enter your choice: ", 1, 5);
        
        switch (choice) {
            case 1:
                showProductMenu();
                break;
            case 2:
                showSalesMenu();
                break;
            case 3:
                showReportsMenu();
                break;
            case 4:
                showLowStockAlerts();
                break;
            case 5:
                return false;
        }
        
        return true;
    }
    
    /**
     * Display product management menu.
     */
    private static void showProductMenu() {
        boolean back = false;
        
        while (!back) {
            InputHelper.clearScreen();
            System.out.println("=====================================");
            System.out.println("  PRODUCT MANAGEMENT");
            System.out.println("=====================================");
            System.out.println("1. View All Products");
            System.out.println("2. Add New Product");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Search Product by Name");
            System.out.println("6. View Products by Category");
            System.out.println("7. Back to Main Menu");
            System.out.println("=====================================");
            
            int choice = InputHelper.getIntInRange("Enter your choice: ", 1, 7);
            
            switch (choice) {
                case 1:
                    inventoryService.displayAllProducts();
                    InputHelper.pause();
                    break;
                case 2:
                    addProduct();
                    break;
                case 3:
                    updateProduct();
                    break;
                case 4:
                    deleteProduct();
                    break;
                case 5:
                    searchProduct();
                    break;
                case 6:
                    viewByCategory();
                    break;
                case 7:
                    back = true;
                    break;
            }
        }
    }
    
    /**
     * Display sales management menu.
     */
    private static void showSalesMenu() {
        boolean back = false;
        
        while (!back) {
            InputHelper.clearScreen();
            System.out.println("=====================================");
            System.out.println("  SALES MANAGEMENT");
            System.out.println("=====================================");
            System.out.println("1. Process New Sale");
            System.out.println("2. View All Sales");
            System.out.println("3. View Sales by Product");
            System.out.println("4. View Sales by Date Range");
            System.out.println("5. Back to Main Menu");
            System.out.println("=====================================");
            
            int choice = InputHelper.getIntInRange("Enter your choice: ", 1, 5);
            
            switch (choice) {
                case 1:
                    processSale();
                    break;
                case 2:
                    salesService.displayAllSales();
                    InputHelper.pause();
                    break;
                case 3:
                    viewSalesByProduct();
                    break;
                case 4:
                    viewSalesByDateRange();
                    break;
                case 5:
                    back = true;
                    break;
            }
        }
    }
    
    /**
     * Display reports menu.
     */
    private static void showReportsMenu() {
        boolean back = false;
        
        while (!back) {
            InputHelper.clearScreen();
            System.out.println("=====================================");
            System.out.println("  REPORTS & REVENUE");
            System.out.println("=====================================");
            System.out.println("1. Monthly Revenue Summary");
            System.out.println("2. Yearly Revenue Summary");
            System.out.println("3. Back to Main Menu");
            System.out.println("=====================================");
            
            int choice = InputHelper.getIntInRange("Enter your choice: ", 1, 3);
            
            switch (choice) {
                case 1:
                    viewMonthlyRevenue();
                    break;
                case 2:
                    viewYearlyRevenue();
                    break;
                case 3:
                    back = true;
                    break;
            }
        }
    }
    
    // ========== Product Management Methods ==========
    
    /**
     * Add a new product.
     */
    private static void addProduct() {
        System.out.println("\n========== ADD NEW PRODUCT ==========");
        
        String name = InputHelper.getNonEmptyString("Enter product name: ");
        String description = InputHelper.getString("Enter product description (optional): ");
        String category = InputHelper.getNonEmptyString("Enter product category: ");
        BigDecimal price = InputHelper.getBigDecimal("Enter product price: ");
        int quantity = InputHelper.getPositiveInt("Enter product quantity: ");
        int lowStockThreshold = InputHelper.getPositiveInt("Enter low stock threshold: ");
        
        Product product = new Product(name, description, category, price, quantity, lowStockThreshold);
        
        try {
            Product created = inventoryService.addProduct(product);
            System.out.println("Product added successfully! ID: " + created.getId());
        } catch (Exception e) {
            System.err.println("Error adding product: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    /**
     * Update an existing product.
     */
    private static void updateProduct() {
        System.out.println("\n========== UPDATE PRODUCT ==========");
        
        inventoryService.displayAllProducts();
        
        int productId = InputHelper.getInt("Enter product ID to update: ");
        
        try {
            Product product = inventoryService.getProduct(productId);
            
            System.out.println("\nCurrent details:");
            System.out.println(product);
            
            System.out.println("\nEnter new details (press Enter to keep current value):");
            
            String name = InputHelper.getString("Name [" + product.getName() + "]: ");
            if (!name.isEmpty()) product.setName(name);
            
            String description = InputHelper.getString("Description [" + product.getDescription() + "]: ");
            if (!description.isEmpty()) product.setDescription(description);
            
            String category = InputHelper.getString("Category [" + product.getCategory() + "]: ");
            if (!category.isEmpty()) product.setCategory(category);
            
            String priceStr = InputHelper.getString("Price [" + product.getPrice() + "]: ");
            if (!priceStr.isEmpty()) product.setPrice(new BigDecimal(priceStr));
            
            String qtyStr = InputHelper.getString("Quantity [" + product.getQuantity() + "]: ");
            if (!qtyStr.isEmpty()) product.setQuantity(Integer.parseInt(qtyStr));
            
            String thresholdStr = InputHelper.getString("Low Stock Threshold [" + product.getLowStockThreshold() + "]: ");
            if (!thresholdStr.isEmpty()) product.setLowStockThreshold(Integer.parseInt(thresholdStr));
            
            boolean updated = inventoryService.updateProduct(product);
            if (updated) {
                System.out.println("Product updated successfully!");
            }
            
        } catch (ProductNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error updating product: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    /**
     * Delete a product.
     */
    private static void deleteProduct() {
        System.out.println("\n========== DELETE PRODUCT ==========");
        
        inventoryService.displayAllProducts();
        
        int productId = InputHelper.getInt("Enter product ID to delete: ");
        
        try {
            boolean confirmed = InputHelper.getConfirmation("Are you sure you want to delete this product?");
            if (confirmed) {
                boolean deleted = inventoryService.deleteProduct(productId);
                if (deleted) {
                    System.out.println("Product deleted successfully!");
                }
            } else {
                System.out.println("Deletion cancelled.");
            }
        } catch (ProductNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error deleting product: " + e.getMessage());
        }
        
        InputHelper.pause();
    }
    
    /**
     * Search products by name.
     */
    private static void searchProduct() {
        String searchTerm = InputHelper.getString("Enter product name to search: ");
        
        List<Product> products = inventoryService.searchProducts(searchTerm);
        
        if (products.isEmpty()) {
            System.out.println("No products found matching: " + searchTerm);
        } else {
            System.out.println("\n========== SEARCH RESULTS ==========");
            for (Product p : products) {
                System.out.println(p);
            }
        }
        
        InputHelper.pause();
    }
    
    /**
     * View products by category.
     */
    private static void viewByCategory() {
        String category = InputHelper.getString("Enter category: ");
        
        List<Product> products = inventoryService.getProductsByCategory(category);
        
        if (products.isEmpty()) {
            System.out.println("No products found in category: " + category);
        } else {
            System.out.println("\n========== CATEGORY: " + category.toUpperCase() + " ==========");
            for (Product p : products) {
                System.out.println(p);
            }
        }
        
        InputHelper.pause();
    }
    
    // ========== Sales Management Methods ==========
    
    /**
     * Process a new sale.
     */
    private static void processSale() {
        System.out.println("\n========== PROCESS SALE ==========");
        
        inventoryService.displayAllProducts();
        
        int productId = InputHelper.getInt("\nEnter product ID: ");
        int quantity = InputHelper.getInt("Enter quantity: ");
        
        try {
            salesService.processSale(productId, quantity);
        } catch (ProductNotFoundException e) {
            System.err.println("Error: " + e.getMessage());
        } catch (InsufficientStockException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Available quantity: " + e.getAvailableQuantity());
        } catch (Exception e) {
            System.err.println("Error processing sale: " + e.getMessage());
        }
        
        // Check for low stock after sale
        inventoryService.checkLowStockAlerts();
        
        InputHelper.pause();
    }
    
    /**
     * View sales by product.
     */
    private static void viewSalesByProduct() {
        inventoryService.displayAllProducts();
        
        int productId = InputHelper.getInt("Enter product ID: ");
        
        List<com.inventory.model.Sale> sales = salesService.getSalesByProduct(productId);
        
        if (sales.isEmpty()) {
            System.out.println("No sales found for this product.");
        } else {
            System.out.println("\n========== SALES FOR PRODUCT ==========");
            for (com.inventory.model.Sale s : sales) {
                System.out.println(s);
            }
        }
        
        InputHelper.pause();
    }
    
    /**
     * View sales by date range.
     */
    private static void viewSalesByDateRange() {
        System.out.println("\n========== SALES BY DATE RANGE ==========");
        
        String startDate = InputHelper.getString("Enter start date (yyyy-MM-dd): ");
        String endDate = InputHelper.getString("Enter end date (yyyy-MM-dd): ");
        
        List<com.inventory.model.Sale> sales = salesService.getSalesByDateRange(startDate, endDate);
        
        if (sales.isEmpty()) {
            System.out.println("No sales found in this date range.");
        } else {
            System.out.println("\n========== SALES FROM " + startDate + " TO " + endDate + " ==========");
            for (com.inventory.model.Sale s : sales) {
                System.out.println(s);
            }
        }
        
        InputHelper.pause();
    }
    
    // ========== Reports Methods ==========
    
    /**
     * View monthly revenue.
     */
    private static void viewMonthlyRevenue() {
        int year = InputHelper.getInt("Enter year (yyyy): ");
        int month = InputHelper.getIntInRange("Enter month (1-12): ", 1, 12);
        
        salesService.displayMonthlyRevenue(year, month);
        InputHelper.pause();
    }
    
    /**
     * View yearly revenue.
     */
    private static void viewYearlyRevenue() {
        int year = InputHelper.getInt("Enter year (yyyy): ");
        
        salesService.displayYearlyRevenue(year);
        InputHelper.pause();
    }
    
    /**
     * Show low stock alerts.
     */
    private static void showLowStockAlerts() {
        inventoryService.checkLowStockAlerts();
        InputHelper.pause();
    }
}
