package de.thws.Adapters.web_in.dto;

import java.time.LocalDateTime;

public class UserResponse {
    private final Long userId;
    private final String username;
    private final String email;
    private final LocalDateTime createdAt;

    public UserResponse(Long userId, String username, String email, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.createdAt = createdAt;
    }

    public Long getUserId() {
        return userId;
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
}
