package de.thws.Application.Domain.DomainModels;

public enum TaskPriority {
    LOW(1), MEDIUM(2), HIGH(3), URGENT(4);
//MEDIUM - default
    private final int level;
    private TaskPriority(int level) {
        this.level = level;
    }
    public int getLevel() {
        return level;
    }
}
