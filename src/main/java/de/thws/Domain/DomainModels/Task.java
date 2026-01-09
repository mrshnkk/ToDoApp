package de.thws.Domain.DomainModels;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;



public class Task {

    private String title;
    private String description;
    private LocalDate deadline;
    private TaskPriority priority;
    private TaskStatus status;
    private Project project; //can be nullable

    private Set<String> tags = new HashSet<>();

    private final LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private User assignedUser;

    private Notification notification;



    public Task(String title) {

        if (title == null) {

            throw new IllegalArgumentException("Title is required");

        }

        this.title = title;

        this.priority = TaskPriority.MEDIUM;  //set default values (our tasks cannot exist without priorities and statuses)

        this.status = TaskStatus.TODO;

        this.createdAt = LocalDateTime.now();

        this.updatedAt = createdAt;

    }



    private void touch(){

        this.updatedAt = LocalDateTime.now();

    }



    public void renameTask(String newTitle) {

        if (newTitle.isEmpty()) {

            throw new IllegalArgumentException("Task title cannot be empty");

        } else {

            this.title = newTitle;

            touch();

        }

    }

    public void assignToProject(Project project) {

        this.project = project;

        touch();

    }



    public void changeDescription(String newDescription){

        this.description= newDescription;

        touch();

    }



    public void changehangePriority(TaskPriority newPriority){

        this.priority= newPriority;

        touch();

    }



    public void changeStatus(TaskStatus newStatus){

        this.status = newStatus;

        touch();

    }



    public void assignToUser(User user){

        this.assignedUser = user;

        touch();

    }



    public void addTag(String newTag){

        if (newTag==null){

            return;

        }

        this.tags.add(newTag.trim().toLowerCase());

        touch();

    }

    public void removeTag(String tag) {

        if (tag == null) {

            return;

        }

        tags.remove(tag.trim().toLowerCase());

        touch();

    }





    public void setDeadline(LocalDate newDeadline){

        if (newDeadline == null){

            throw new IllegalArgumentException("Deadline cannot be null");

        }

        if (newDeadline.isBefore(LocalDate.now())){

            throw new IllegalArgumentException("Deadline cannot be in the past");

        }

        this.deadline = newDeadline;

        touch();

    }





    public void scheduleReminder(LocalDateTime reminderTime, LocalDateTime now){

        this.notification= Notification.schedule(reminderTime, now);

        touch();

    }



    public void cancelReminder() {

        if (notification == null) {

            throw new IllegalStateException("No reminder to cancel");

        }

        notification.cancel();

        touch();

    }



    public String getTitle() {

        return title;

    }



    public LocalDateTime getCreatedAt() {

        return createdAt;

    }



    public TaskStatus getStatus() {

        return status;

    }

}
