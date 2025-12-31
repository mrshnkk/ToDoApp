package de.thws.Adapters.database_out;

import de.thws.Domain.DomainModels.User;
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
            mappedBy = "Project",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<TaskEntity> tasks = new ArrayList<>();


    @Column(name = "Ending_Time", nullable = false)
    private LocalDate endDate;


    @ManyToOne(optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;


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

    public LocalDate getEndDate() {
        return endDate;

    }

    public List<TaskEntity> getTasks() {
        return tasks;

    }

}
