package de.thws.Application.Domain.Services;

public class DuplicateTeamMemberException extends RuntimeException {
    public DuplicateTeamMemberException(String message) {
        super(message);
    }
}
