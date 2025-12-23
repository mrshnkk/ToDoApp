package de.thws.Domain.DomainModels;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Project {
    private final Long projectId;
    String name;
    String description;
    LocalDateTime startDate;
    private LocalDate endDate;
    private final Long ownerId;

    public Project(Long projectId, String name, String description, LocalDateTime startDate, LocalDate endDate, Long ownerId) {
        this.projectId = projectId;
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.ownerId = ownerId;
    }
    public void createProject(){
        Project project = new Project(this.projectId, this.name, this.description, this.startDate, this.endDate, ownerId);
    }
    public void updateProject(String name, String description, LocalDateTime startDate, LocalDate endDate){
        this.name = name;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    public void deleteProject(){
    }
    public double getProgress() {
        //TODO Progress count
        return 0;
    }

}
