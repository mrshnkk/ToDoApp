package de.thws.Domain.Services;

public class InvalidTaskFilterException extends IllegalArgumentException {
    public InvalidTaskFilterException(String message) {
        super(message);
    }
}
