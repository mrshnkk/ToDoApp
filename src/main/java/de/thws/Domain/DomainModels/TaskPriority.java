package de.thws.Domain.DomainModels;

public enum TaskPriority {
    LOW(1), MEDIUM(2), HIGH(3), URGENT(4);

    private final int level;
    private TaskPriority(int level) {
        this.level = level;
    }
    public int getLevel() {
        return level;
    }
}
