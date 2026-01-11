package de.thws.Application.Domain.Services;

public class EmptyTaskTitleException extends RuntimeException {
    public EmptyTaskTitleException(String message) {
        super(message);
    }
}
