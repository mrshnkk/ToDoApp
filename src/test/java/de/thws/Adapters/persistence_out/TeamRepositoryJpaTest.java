package de.thws.Adapters.persistence_out;

import de.thws.Application.Domain.DomainModels.Team;
import de.thws.Application.Domain.DomainModels.TeamRole;
import de.thws.Application.Domain.DomainModels.User;
import de.thws.Application.Ports.out.TeamRepository;
import de.thws.Application.Ports.out.UserRepository;
import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
class TeamRepositoryJpaTest {

    @Inject
    TeamRepository teamRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    EntityManager entityManager;

    @Test
    @TestTransaction
    void saveAndQueryTeams() {
        User owner = new User("teamowner1", "teamowner1@test.com", "Abcdef!1");
        userRepository.save(owner);

        User member = new User("teammember1", "teammember1@test.com", "Abcdef!1");
        userRepository.save(member);

        Team team = new Team("Team Alpha", "desc", owner);
        teamRepository.save(team);

        assertNotNull(team.getTeamId());

        TeamEntity teamEntity = entityManager.find(TeamEntity.class, team.getTeamId());
        UserEntity memberEntity = entityManager.find(UserEntity.class, member.getUserId());
        entityManager.persist(new TeamMemberEntity(teamEntity, memberEntity, TeamRole.MEMBER, LocalDateTime.now()));

        List<Team> byOwner = teamRepository.findByOwnerId(owner.getUserId());
        assertEquals(1, byOwner.size());

        List<Team> byUser = teamRepository.findByUserId(member.getUserId());
        assertEquals(1, byUser.size());
    }
}
