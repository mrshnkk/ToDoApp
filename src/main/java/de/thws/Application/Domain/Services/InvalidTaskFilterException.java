package de.thws.Application.Domain.Services;

public class InvalidTaskFilterException extends IllegalArgumentException {
    public InvalidTaskFilterException(String message) {
        super(message);
    }
}
