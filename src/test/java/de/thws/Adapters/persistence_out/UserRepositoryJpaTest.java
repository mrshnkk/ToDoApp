package de.thws.Adapters.persistence_out;

import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.out.UserRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@QuarkusTest
class UserRepositoryJpaTest {

    @Inject
    UserRepository userRepository;

    @Test
    @TestTransaction
    void saveAndFindUser() {
        User user = new User("userrepo1", "userrepo1@test.com", "Abcdef!1");

        userRepository.save(user);

        assertNotNull(user.getUserId());

        Optional<User> byId = userRepository.findById(user.getUserId());
        assertTrue(byId.isPresent());
        assertEquals("userrepo1", byId.get().getUsername());

        Optional<User> byUsername = userRepository.findByUsername("userrepo1");
        assertTrue(byUsername.isPresent());
        assertEquals("userrepo1@test.com", byUsername.get().getEmail());

        Optional<User> byEmail = userRepository.findByEmail("userrepo1@test.com");
        assertTrue(byEmail.isPresent());
        assertEquals("userrepo1", byEmail.get().getUsername());
    }
}
