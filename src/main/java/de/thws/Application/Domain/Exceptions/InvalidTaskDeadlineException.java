package de.thws.Application.Domain.Exceptions;

public class InvalidTaskDeadlineException extends RuntimeException {
    public InvalidTaskDeadlineException(String message) {
        super(message);
    }
}
