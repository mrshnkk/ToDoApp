package de.thws.Ports.output;

import de.thws.Domain.DomainModels.User;
import java.util.Optional;

//TODO implementation of UserRepository -> adapter layer
public interface UserRepository {
    Optional<User> findById(Long userId); //Optional<User> is used to prevent NullPointerException
    Optional<User>  findByUsername(String username);
    Optional<User> findByEmail(String email);
    void save(User user);
    void deleteById(Long userId);
}
