package de.thws.Application.Domain.DomainModels;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;

public class User {
    private Long userId;
    private String username; //identity of the user model -> id is going to be generated in the persistence layer.
    private String email;
    private String password;
    private final LocalDateTime createdAt;

    public User(String username, String email, String password) {
        validateEmail(email);
        validateUsername(username);
        validatePassword(password);

            this.username = username;
            this.email = email;
            this.password = hashPassword(password);
            this.createdAt = LocalDateTime.now();
        }

    private User(Long userId, String username, String email, String passwordHash, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = passwordHash;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
    }

    public static User fromPersisted(Long userId, String username, String email, String passwordHash, LocalDateTime createdAt) {
        return new User(userId, username, email, passwordHash, createdAt);
    }


    public String getUsername() {
        return username;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        if (this.userId != null) {
            throw new IllegalStateException("User ID already set");
        }
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return password;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void changePassword (String newPassword){
        validatePassword(newPassword);
        this.password = hashPassword(newPassword);
    }

    public void changeUsername(String newUsername){
        validateUsername(newUsername);
        this.username=newUsername;
    }

    public void changeEmail(String newEmail){
        validateEmail(newEmail);
        this.email=newEmail;
    }

    public void validatePassword(String password) {
        if (password == null) {
            throw new IllegalArgumentException("Password can not be null");
        }
        if (password.length() < 8 || password.length() > 25) {
            throw new IllegalArgumentException("Password has to be between 8 and 25 characters");
        }
        boolean hasLowerCase = false;
        boolean hasUpperCase = false;
        boolean hasSpecialChar = false;

        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (c >= 'a' && c <= 'z') {
                hasLowerCase = true;
            } else if (c >= 'A' && c <= 'Z') {
                hasUpperCase = true;
            } else if (!Character.isDigit(c)) {
                hasSpecialChar = true;
            }
        }
            if (!hasLowerCase || !hasUpperCase || !hasSpecialChar) {
                throw new IllegalArgumentException("Password must contain at least one lowercase letter, one uppercase letter and one special character");
            }
    }

    public void  validateUsername(String username){
        if (username == null) {
            throw new IllegalArgumentException("Username is required");
        }
        if (username.length() < 4 || username.length() > 16) {
            throw new IllegalArgumentException("Username has to be between 4 and 16 characters");
        }
        char[] check = username.toCharArray();
        for (int i = 0; i < username.length(); i++) { //username validation;
            if (check[i] == ' ') {
                throw new IllegalArgumentException("Username should not contain spaces.");
            } else {
                i++;
            }
        }
    }

    public void validateEmail(String email) {
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
        if (email.length() < 8 || email.length() > 30) {
            throw new IllegalArgumentException("Mail Address has to be longer than 8 characters or shorter than 30");
        }
    }

        private static String hashPassword (String password){
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
