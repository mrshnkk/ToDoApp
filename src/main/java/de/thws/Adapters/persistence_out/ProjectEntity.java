package de.thws.Adapters.persistence_out;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "Project")
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;


    @Column(name = "Name", nullable = false)
    private String name;


    @Column(name = "Description", length = 1000)
    private String description;


    @OneToMany(
            mappedBy = "project",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TaskEntity> tasks = new ArrayList<>();


    @Column(name = "Ending_Time")
    private LocalDate endDate;


    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Column(name = "team_id")
    private Long teamId;


    public ProjectEntity() {}


    public ProjectEntity(String name, UserEntity owner) {
        this.name = name;
        this.owner = owner;

    }


    public Long getProjectId() {
        return projectId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public UserEntity getOwner() {
        return owner;
    }

    public Long getTeamId() {
        return teamId;
    }

    public LocalDate getEndDate() {
        return endDate;

    }

    public List<TaskEntity> getTasks() {
        return tasks;

    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setOwner(UserEntity owner) {
        this.owner = owner;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

}
