package com.inventory.util;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

/**
 * Utility class for handling user input.
 */
public class InputHelper {
    private static Scanner scanner = new Scanner(System.in);
    
    /**
     * Get a string input from user.
     * @param prompt the prompt to display
     * @return the input string
     */
    public static String getString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
    
    /**
     * Get an integer input from user.
     * @param prompt the prompt to display
     * @return the input integer
     */
    public static int getInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
    
    /**
     * Get a double input from user.
     * @param prompt the prompt to display
     * @return the input double
     */
    public static double getDouble(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }
    
    /**
     * Get a BigDecimal input from user.
     * @param prompt the prompt to display
     * @return the input BigDecimal
     */
    public static BigDecimal getBigDecimal(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                BigDecimal value = new BigDecimal(input);
                if (value.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("Value must be positive. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number.");
            }
        }
    }
    
    /**
     * Get a positive integer input from user.
     * @param prompt the prompt to display
     * @return the input positive integer
     */
    public static int getPositiveInt(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("Value must be positive. Please try again.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
    
    /**
     * Get a non-empty string input from user.
     * @param prompt the prompt to display
     * @return the input string (non-empty)
     */
    public static String getNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Input cannot be empty. Please try again.");
                continue;
            }
            return input;
        }
    }
    
    /**
     * Get a date input from user in yyyy-MM-dd format.
     * @param prompt the prompt to display
     * @return the input LocalDate
     */
    public static LocalDate getDate(String prompt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        
        while (true) {
            try {
                System.out.print(prompt);
                String input = scanner.nextLine().trim();
                return LocalDate.parse(input, formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd format.");
            }
        }
    }
    
    /**
     * Get a yes/no confirmation from user.
     * @param prompt the prompt to display
     * @return true if yes, false if no
     */
    public static boolean getConfirmation(String prompt) {
        while (true) {
            System.out.print(prompt + " (yes/no): ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (input.equals("yes") || input.equals("y")) {
                return true;
            } else if (input.equals("no") || input.equals("n")) {
                return false;
            } else {
                System.out.println("Invalid input. Please enter 'yes' or 'no'.");
            }
        }
    }
    
    /**
     * Get an integer within a range.
     * @param prompt the prompt to display
     * @param min the minimum value
     * @param max the maximum value
     * @return the input integer within range
     */
    public static int getIntInRange(String prompt, int min, int max) {
        while (true) {
            int value = getInt(prompt);
            if (value >= min && value <= max) {
                return value;
            }
            System.out.println("Input must be between " + min + " and " + max + ".");
        }
    }
    
    /**
     * Pause execution until user presses Enter.
     */
    public static void pause() {
        System.out.print("\nPress Enter to continue...");
        scanner.nextLine();
    }
    
    /**
     * Clear the console screen (print new lines).
     */
    public static void clearScreen() {
        for (int i = 0; i < 50; i++) {
            System.out.println();
        }
    }
    
    /**
     * Close the scanner.
     */
    public static void close() {
        if (scanner != null) {
            scanner.close();
        }
    }
}
