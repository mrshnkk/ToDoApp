package de.thws.Adapters.database_out;
import de.thws.Domain.DomainModels.TeamRole;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Team_Members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"TeamId", "UserId"})
})
public class TeamMemberEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="Member_Id")
    private Long memberId;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "Team_id")
    private TeamEntity team;

    @ManyToOne(optional = false)
    @JoinColumn(name = "User_id")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name="Team_Role", nullable = false)
    private TeamRole role;
    
    @Column(name="Joined_At", nullable=false)
    private LocalDateTime joinedAt;

    
    public TeamMemberEntity(TeamEntity team, UserEntity user, TeamRole role){
        this.team=team;
        this.user= user;
        this.role=role;
        this.joinedAt=LocalDateTime.now();
    }
    public TeamMemberEntity(){}

    public Long getMemberId() {
        return memberId;
    }

    public UserEntity getUser() {
        return user;
    }

    public TeamRole getRole() {
        return role;
    }

    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }

    public void setTeamMemberId(Long memberId) {
        if (this.memberId!= null) {
            throw new IllegalStateException("ID already set");
        }
        this.memberId= memberId;
    }

}
