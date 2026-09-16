package com.steve.librarylendingservice.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class PasswordHandler {

    @Value("${crypto.algorithm}")
    private String ALGORITHM;

    // Injects your secret key from application.properties or application.yml
    @Value("${crypto.secret-key}")
    private String secretKey;

    /**
     * Hashes a password using a salt and the application's secret key.
     *
     * @param password The raw password to hash.
     * @param salt     The unique salt associated with the user.
     * @return The hex-encoded HMAC-SHA256 string.
     */
    public String hashPassword(String password, String salt) {
        try {
            String inputToHash = password + salt;

            SecretKeySpec keySpec = new SecretKeySpec(
                    secretKey.getBytes(StandardCharsets.UTF_8),
                    ALGORITHM
            );

            Mac mac = Mac.getInstance(ALGORITHM);
            mac.init(keySpec);

            byte[] rawHmacBytes = mac.doFinal(inputToHash.getBytes(StandardCharsets.UTF_8));

            // Convert to Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte b : rawHmacBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to compute password hash", e);
        }
    }

    /**
     * Verifies a raw password against an existing hash using constant-time comparison.
     *
     * @param password     The raw password to verify.
     * @param salt         The unique salt associated with the user.
     * @param expectedHash The stored hash to compare against.
     * @return true if the password matches, false otherwise.
     */
    public boolean verifyPassword(String password, String salt, String expectedHash) {
        String generatedHash = hashPassword(password, salt);

        // Use MessageDigest.isEqual for constant-time comparison to prevent timing attacks
        return MessageDigest.isEqual(
                generatedHash.getBytes(StandardCharsets.UTF_8),
                expectedHash.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateNewSalt() {
        SecureRandom random = new SecureRandom();
        byte[] saltBytes = new byte[16]; // 128-bit salt
        random.nextBytes(saltBytes);
        return Base64.getEncoder().encodeToString(saltBytes);
    }
}
