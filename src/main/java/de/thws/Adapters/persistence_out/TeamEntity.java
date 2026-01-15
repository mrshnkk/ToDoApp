package de.thws.Adapters.persistence_out;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Teams")
public class TeamEntity {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="TeamId")
    private Long teamId;

    @Column(name="Team_Name", nullable= false)
    private String teamName;

    @Column(name="Description", length = 1000)
    private String description;

    @Column(name="Creation_date", nullable = false)
    private LocalDateTime createdAt;

    @OneToMany //relationship -> says there can be multiple members in one team
            (mappedBy = "team", //links to the attribute "team" in TeamMemberEntity -> here's as a FK. Required because otherwise JPA created a new unnecessary table.
            cascade = CascadeType.ALL, //all operations from TeamEntity will be applied to the TeamMemberEntity
            orphanRemoval = true) //if a member is deleted from TeamMembers -> the line is deleted from the Database as well
    private List<TeamMemberEntity> teamMembers = new ArrayList<>();


    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    public TeamEntity(String teamName, UserEntity owner, LocalDateTime createdAt) {
        this.teamName= teamName;
        this.owner = owner;
        this.createdAt = createdAt;
    }

    public TeamEntity(){}
    public UserEntity getOwner() {
        return owner;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public void setTeamId(Long id) {
        if (this.teamId != null) {
            throw new IllegalStateException("ID already set");
        }
        this.teamId = id;
    }

}
