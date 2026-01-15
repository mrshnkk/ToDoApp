package de.thws.Application.Domain.DomainModels;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Project {
    private Long projectId;
    String name;
    String description;
    final List<Task> tasks = new ArrayList<>();
    LocalDateTime startDate;
    private LocalDate endDate;
    private final User owner;
    private Long teamId;

    public Project(String name, User owner) {
        if (name == null) {
            throw new IllegalArgumentException("Project name is required");
        }
        if (owner == null) {
            throw new IllegalArgumentException("Project must have an owner");
        }
        this.name = name;
        this.owner = owner;
        this.startDate = LocalDateTime.now();
    }



    public void addTask(Task task){
        if (task == null){
            throw new IllegalArgumentException("Task cannot be null");
        }
        if (tasks.contains(task)){
            throw new IllegalArgumentException("Task already exists");
        }
        tasks.add(task);
    }



    public void deleteTask(Task task){
        if (task == null){
            throw new IllegalArgumentException("Task cannot be null");
        }
        boolean removed = tasks.remove(task);
        if (!removed) {
            throw new IllegalStateException("Task not found in this project");
        }
    }



    public void updateDescription(String newDescription) {
        if (newDescription == null) {
            this.description = null;
            return;
        }
        this.description = newDescription;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }


    public User getOwner() {
        return owner;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        if (this.projectId != null) {
            throw new IllegalStateException("Project ID already set");
        }
        this.projectId = projectId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        if (this.teamId != null) {
            throw new IllegalStateException("Team ID already set");
        }
        this.teamId = teamId;
    }

    //TODO  calculateProgress
    //public double calculateProgress() {}

    //TODO hasActiveTasks
    //public boolean hasActiveTasks(){}





}
