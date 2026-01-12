package de.thws.Application.Ports.in;

import de.thws.Application.Domain.DomainModels.User;

import java.util.Optional;

public interface UserUseCase {
    User create(User user);
    Optional<User> findById(Long userId);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    void delete(Long userId);
}
