package de.thws.Application.Domain.Services;

import de.thws.Application.Domain.DomainModels.*;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Set;

public final class TaskFieldAccessor {
    private static final Field PRIORITY_FIELD = getField("priority");
    private static final Field PROJECT_FIELD = getField("project");
    private static final Field TAGS_FIELD = getField("tags");

    private TaskFieldAccessor() {
    }

    public static TaskPriority getPriority(Task task) {
        return readField(task, PRIORITY_FIELD, TaskPriority.class);
    }

    public static Project getProject(Task task) {
        return readField(task, PROJECT_FIELD, Project.class);
    }

    public static Set<String> getTags(Task task) {
        Set<String> tags = readField(task, TAGS_FIELD, Set.class);
        if (tags == null) {
            return Collections.emptySet();
        }
        return tags;
    }

    private static Field getField(String name) {
        try {
            Field field = Task.class.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException ex) {
            throw new IllegalStateException("Task field not found: " + name, ex);
        }
    }

    private static <T> T readField(Task task, Field field, Class<T> type) {
        try {
            Object value = field.get(task);
            return type.cast(value);
        } catch (IllegalAccessException ex) {
            throw new IllegalStateException("Cannot access field: " + field.getName(), ex);
        }
    }
}
