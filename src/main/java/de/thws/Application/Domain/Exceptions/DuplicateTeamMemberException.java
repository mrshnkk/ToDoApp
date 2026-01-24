package de.thws.Application.Domain.Exceptions;

public class DuplicateTeamMemberException extends RuntimeException {
    public DuplicateTeamMemberException(String message) {
        super(message);
    }
}
