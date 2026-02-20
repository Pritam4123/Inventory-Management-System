package com.inventory.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * Utility class for secure password handling.
 * Uses PBKDF2 with SHA-256 and a random salt for password hashing.
 */
public class PasswordUtil {
    
    private static final int SALT_LENGTH = 16;
    private static final int ITERATIONS = 10000;
    private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
    
    /**
     * Hash a password with a random salt.
     * @param password the plain text password
     * @return the hashed password (salt:hash format)
     */
    public static String hashPassword(String password) {
        try {
            // Generate random salt
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[SALT_LENGTH];
            random.nextBytes(salt);
            
            // Create hash using PBKDF2
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hash = md.digest(password.getBytes());
            
            // Apply multiple iterations
            for (int i = 1; i < ITERATIONS; i++) {
                md.reset();
                hash = md.digest(hash);
            }
            
            // Encode as Base64 and prepend salt
            String encodedSalt = Base64.getEncoder().encodeToString(salt);
            String encodedHash = Base64.getEncoder().encodeToString(hash);
            
            return encodedSalt + ":" + encodedHash;
            
        } catch (NoSuchAlgorithmException e) {
 throw new RuntimeException("Password hashing failed: " + e.getMessage(), e); }
    }
    
    /**
     * Verify a plain text password against a stored hash.
     * @param plainPassword the plain text password to verify
     * @param storedHash the stored hashed password (salt:hash format)
     * @return true if passwords match, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String storedHash) {
        if (plainPassword == null || storedHash == null) {
            return false;
        }
        
        try {
            // Split salt and hash
            String[] parts = storedHash.split(":");
            if (parts.length != 2) {
                return false;
            }
            
            byte[] salt = Base64.getDecoder().decode(parts[0]);
            byte[] storedHashBytes = Base64.getDecoder().decode(parts[1]);
            
            // Hash the input password with the same salt
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            md.update(salt);
            byte[] hash = md.digest(plainPassword.getBytes());
            
            // Apply same iterations
            for (int i = 1; i < ITERATIONS; i++) {
                md.reset();
                hash = md.digest(hash);
            }
            
            // Compare hashes
            return MessageDigest.isEqual(hash, storedHashBytes);
            
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Password verification failed: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if a password needs to be rehashed.
     * Currently always returns false.
     * @param storedHash the stored hashed password
     * @return false
     */
    public static boolean needsRehash(String storedHash) {
        return false;
    }
}
