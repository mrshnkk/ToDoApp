package de.thws.Application.Services;

import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.in.UserUseCase;
import de.thws.Application.Ports.out.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.Optional;

@ApplicationScoped
public class UserUseCaseImpl implements UserUseCase {
    private final UserRepository userRepository;

    @Inject
    public UserUseCaseImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User create(User user) {
        userRepository.save(user);
        return user;
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void delete(Long userId) {
        userRepository.deleteById(userId);
    }
}
