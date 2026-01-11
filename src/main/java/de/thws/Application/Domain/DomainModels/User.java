package de.thws.Application.Domain.DomainModels;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class User {
    private String username; //identity of the user -> id is going to be generated in the persistence layer.
    private String email;
    private String password;
    private final LocalDateTime createdAt;

    private User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("Password too short");
        }
        this.password = hashPassword(newPassword);
    }

    public User createUser() {
        if (username == null) {
            throw new IllegalArgumentException("Username is required");
        }
        if (username.length() < 4 || username.length() > 16) {
            throw new IllegalArgumentException("Username has to be between 4 and 16 characters");
        }
        char[] check = this.username.toCharArray();
        for (int i = 0; i < username.length(); i++) { //username validation;
            if (check[i] == ' ') {
                throw new IllegalArgumentException("Username should not contain spaces.");
            } else {
                i++;
            }
        }

        if (email == null) {
            throw new IllegalArgumentException("Email is required");
        }
        int countAt = 0;
        int countDot = 0;
        char[] mailCheck = email.toCharArray();
        for (int i = 0; i < email.length(); i++) {
            if (mailCheck[i] == '@') {
                countAt++;
            }
            if (mailCheck[i] == '.') {
                countDot++;
            }
        }
        if (countAt != 1 && countDot == 0) {
            throw new IllegalArgumentException("Mail has to contain at and dot signs.");
        }
        if (email.length() < 8 || email.length() > 30){
            throw new IllegalArgumentException("Mailaddress has to be longer than 8 characters or shorter than 30");
        }

        if (password.length() < 8 || password.length() > 25) {
            throw new IllegalArgumentException("Password has to be between 8 and 25 characters");
        }
        if (password == null){
            throw new IllegalArgumentException("Password can not be null");
        }
        return new User(
                username.trim(),
                email.trim(),
                hashPassword(password)
        );
    }


    private static String hashPassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password can not be null");
        }
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte b : hash) {
                String part = Integer.toHexString(b & 0xff);
                if (part.length() == 1) {
                    hex.append('0');
                }
                hex.append(part);
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 not available", e);
        }
    }
}

