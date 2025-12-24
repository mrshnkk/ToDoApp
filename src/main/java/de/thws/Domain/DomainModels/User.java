package de.thws.Domain.DomainModels;
import java.time.LocalDateTime;

public class User{
    private String username; //identity of the user -> id is going to be generated in the persistence layer.
    private String email;
    private String passwordHash;
    private final LocalDateTime createdAt;

    private User(String username, String email, String passwordHash) {
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
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
        this.passwordHash = hashPassword(newPassword);
    }
    public static User createUser(String username, String email, String newPassword){
        if (username == null ) {
            throw new IllegalArgumentException("Username is required");
        }
        if (email == null ) {
            throw new IllegalArgumentException("Email is required");
        }
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("Password too short");
        }
        return new User(
                username.trim(),
                email.trim(),
                hashPassword(newPassword)
        );
    }


    private static String hashPassword(String password){
        //TODO logic to hash the Password
        return password;
    }
}


