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
    public String getPasswordHash() {
        return passwordHash;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void changePassword(String newPasswordHash){
        this.passwordHash= newPasswordHash;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public static User createUser(String username, String email, String password){
        String passwordHash= hashPassword(password);
        LocalDateTime createdAt= LocalDateTime.now();
        return new User(username, email, passwordHash);
    }
    private static String hashPassword(String password){
        //TODO logic to hash the Password
        return password;
    }
}


