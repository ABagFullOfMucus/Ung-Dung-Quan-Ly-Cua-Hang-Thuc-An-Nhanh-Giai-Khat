package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Account {
    private String username;
    private String passwordHash;

    public Account() {
    }

    public Account(String username, String rawPassword) {
        this.username = username;
        this.passwordHash = hash(rawPassword);
    }

    public boolean authenticate(String rawPassword) {
        return passwordHash != null && passwordHash.equals(hash(rawPassword));
    }

    public boolean changePassword(String oldPassword, String newPassword) {
        if (!authenticate(oldPassword) || newPassword == null || newPassword.isEmpty()) {
            return false;
        }
        this.passwordHash = hash(newPassword);
        return true;
    }

    public static String hash(String rawPassword) {
        if (rawPassword == null) {
            return null;
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(bytes.length * 2);
            for (byte b : bytes) {
                builder.append(String.format("%02x", b));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            return rawPassword;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
