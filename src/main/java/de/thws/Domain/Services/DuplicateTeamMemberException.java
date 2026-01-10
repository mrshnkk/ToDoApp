package de.thws.Domain.Services;

public class DuplicateTeamMemberException extends RuntimeException {
    public DuplicateTeamMemberException(String message) {
        super(message);
    }
}
