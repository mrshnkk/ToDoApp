package de.thws.Application.Domain.Services;

public class InvalidTaskDeadlineException extends RuntimeException {
    public InvalidTaskDeadlineException(String message) {
        super(message);
    }
}
