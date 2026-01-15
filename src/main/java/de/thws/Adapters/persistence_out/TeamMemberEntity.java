package de.thws.Adapters.persistence_out;
import de.thws.Application.Domain.DomainModels.TeamRole;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name="Team_Members", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"Team_Id", "User_Id"})
})
public class TeamMemberEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="Member_Id")
    private Long memberId;
    
    @ManyToOne(optional = false)
    @JoinColumn(name = "Team_Id", referencedColumnName= "TeamId")
    private TeamEntity team;

    @ManyToOne(optional = false)
    @JoinColumn(name = "User_Id", referencedColumnName= "UserId")
    private UserEntity user;

    @Enumerated(EnumType.STRING)
    @Column(name="Team_Role", nullable = false)
    private TeamRole role;
    
    @Column(name="Joined_At", nullable=false)
    private LocalDateTime joinedAt;

    
    public TeamMemberEntity(TeamEntity team, UserEntity user, TeamRole role, LocalDateTime joinedAt){
        this.team=team;
        this.user= user;
        this.role=role;
        this.joinedAt=joinedAt;
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
