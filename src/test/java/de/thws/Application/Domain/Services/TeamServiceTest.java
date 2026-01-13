package de.thws.Application.Domain.Services;

import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Domain.DomainModels.TeamMember;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.out.TeamRepository;
import de.thws.Application.Ports.out.UserRepository;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TeamServiceTest {

    @Test
    void createTeamWithDescription() {
        InMemoryTeamRepository teamRepo = new InMemoryTeamRepository();
        InMemoryUserRepository userRepo = new InMemoryUserRepository();
        TeamService service = new TeamService(teamRepo, userRepo);

        User owner = new TestUser("owner1", "owner1@test.com", "Abcdef!1");
        User member = new TestUser("member1", "member1@test.com", "Abcdef!1");
        userRepo.addUser(1L, owner);
        userRepo.addUser(2L, member);

        Team team = service.createTeamWithMembers(1L, "Team A", "desc", List.of("member1"));

        assertEquals("desc", team.getDescription());
        assertEquals(2, team.getTeamMembers().size());
        assertTrue(hasMember(team, "member1"));
    }

    @Test
    void addMembersByUsernameAddsMembers() {
        InMemoryTeamRepository teamRepo = new InMemoryTeamRepository();
        InMemoryUserRepository userRepo = new InMemoryUserRepository();
        TeamService service = new TeamService(teamRepo, userRepo);

        User owner = new TestUser("owner2", "owner2@test.com", "Abcdef!1");
        User member = new TestUser("member2", "member2@test.com", "Abcdef!1");
        userRepo.addUser(10L, owner);
        userRepo.addUser(11L, member);

        Team team = new Team("Team B", "desc", owner);
        teamRepo.save(team);
        Long teamId = team.getTeamId();

        Team updated = service.addMembersByUsername(teamId, List.of("member2"));

        assertEquals(2, updated.getTeamMembers().size());
        assertTrue(hasMember(updated, "member2"));
    }

    @Test
    void addMembersByUsernameThrowsWhenTeamMissing() {
        InMemoryTeamRepository teamRepo = new InMemoryTeamRepository();
        InMemoryUserRepository userRepo = new InMemoryUserRepository();
        TeamService service = new TeamService(teamRepo, userRepo);

        assertThrows(TeamNotFoundException.class,
                () -> service.addMembersByUsername(999L, List.of("member3")));
    }

    @Test
    void createTeamThrowsWhenOwnerMissing() {
        InMemoryTeamRepository teamRepo = new InMemoryTeamRepository();
        InMemoryUserRepository userRepo = new InMemoryUserRepository();
        TeamService service = new TeamService(teamRepo, userRepo);

        assertThrows(UserNotFoundException.class,
                () -> service.createTeamWithMembers(404L, "Team C", "desc", null));
    }

    @Test
    void createTeamThrowsOnDuplicateMember() {
        InMemoryTeamRepository teamRepo = new InMemoryTeamRepository();
        InMemoryUserRepository userRepo = new InMemoryUserRepository();
        TeamService service = new TeamService(teamRepo, userRepo);

        User owner = new TestUser("owner3", "owner3@test.com", "Abcdef!1");
        User member = new TestUser("dupUser", "dup@test.com", "Abcdef!1");
        userRepo.addUser(20L, owner);
        userRepo.addUser(21L, member);

        assertThrows(DuplicateTeamMemberException.class,
                () -> service.createTeamWithMembers(20L, "Team D", "desc", List.of("dupUser", "dupUser")));
    }

    private static boolean hasMember(Team team, String username) {
        for (TeamMember member : team.getTeamMembers()) {
            if (member.getUser().getUsername().equals(username)) {
                return true;
            }
        }
        return false;
    }

    private static void setTeamId(Team team, Long teamId) {
        try {
            Field field = Team.class.getDeclaredField("teamId");
            field.setAccessible(true);
            field.set(team, teamId);
        } catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Unable to set teamId", ex);
        }
    }

    private static class InMemoryTeamRepository implements TeamRepository {
        private final Map<Long, Team> teams = new HashMap<>();
        private long nextId = 1L;

        @Override
        public Team findById(Long teamId) {
            return teams.get(teamId);
        }

        @Override
        public List<Team> findByOwnerId(Long ownerId) {
            return List.of();
        }

        @Override
        public List<Team> findByUserId(Long userId) {
            return List.of();
        }

        @Override
        public void save(Team team) {
            if (team.getTeamId() == null) {
                setTeamId(team, nextId++);
            }
            teams.put(team.getTeamId(), team);
        }

        @Override
        public void delete(Long teamId) {
            teams.remove(teamId);
        }
    }

    private static class InMemoryUserRepository implements UserRepository {
        private final Map<Long, User> usersById = new HashMap<>();
        private final Map<String, User> usersByUsername = new HashMap<>();
        private final Map<String, User> usersByEmail = new HashMap<>();

        void addUser(Long id, User user) {
            usersById.put(id, user);
            usersByUsername.put(user.getUsername(), user);
            usersByEmail.put(user.getEmail(), user);
        }

        @Override
        public Optional<User> findById(Long userId) {
            return Optional.ofNullable(usersById.get(userId));
        }

        @Override
        public Optional<User> findByUsername(String username) {
            return Optional.ofNullable(usersByUsername.get(username));
        }

        @Override
        public Optional<User> findByEmail(String email) {
            return Optional.ofNullable(usersByEmail.get(email));
        }

        @Override
        public void save(User user) {
            throw new UnsupportedOperationException("Not needed for these tests");
        }

        @Override
        public void deleteById(Long userId) {
            throw new UnsupportedOperationException("Not needed for these tests");
        }
    }

    private static class TestUser extends User {
        TestUser(String username, String email, String password) {
            super(username, email, password);
        }

        @Override
        public void validateUsername(String username) {
            if (username == null) {
                throw new IllegalArgumentException("Username is required");
            }
            if (username.length() < 4 || username.length() > 16) {
                throw new IllegalArgumentException("Username has to be between 4 and 16 characters");
            }
            if (username.contains(" ")) {
                throw new IllegalArgumentException("Username should not contain spaces.");
            }
        }
    }
}
