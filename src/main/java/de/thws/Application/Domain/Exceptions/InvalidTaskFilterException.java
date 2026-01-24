package de.thws.Application.Domain.Exceptions;

public class InvalidTaskFilterException extends IllegalArgumentException {
    public InvalidTaskFilterException(String message) {
        super(message);
    }
}
