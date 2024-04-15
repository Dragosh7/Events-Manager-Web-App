package com.example.events.MyPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MyPasswordEncoder {

    public static String encode(String rawPassword) {
        // Implement your custom encoding logic here
        // For example, you can use cryptographic algorithms like SHA-256 or bcrypt
        // Here's a simple example using SHA-256
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder(2 * encodedHash.length);
            for (byte hashByte : encodedHash) {
                String hex = Integer.toHexString(0xff & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error encoding password", e);
        }
    }

    public static boolean matches(String rawPassword, String encodedPassword) {
        // Implement your custom matching logic here
        // Compare the encoded password with the raw password
        String hashedRawPassword = encode(rawPassword);
        return hashedRawPassword.equals(encodedPassword);
    }
}
