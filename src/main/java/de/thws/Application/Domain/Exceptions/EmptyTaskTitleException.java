package de.thws.Application.Domain.Exceptions;

public class EmptyTaskTitleException extends RuntimeException {
    public EmptyTaskTitleException(String message) {
        super(message);
    }
}
