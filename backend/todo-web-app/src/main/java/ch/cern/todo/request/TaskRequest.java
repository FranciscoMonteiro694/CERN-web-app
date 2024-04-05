package ch.cern.todo.request;

import java.time.LocalDateTime;

public record TaskRequest(String taskName, String taskDescription, LocalDateTime deadline, Long categoryId) {

    @Override
    public String taskName() {
        return taskName;
    }

    @Override
    public String taskDescription() {
        return taskDescription;
    }

    @Override
    public LocalDateTime deadline() {
        return deadline;
    }

    @Override
    public Long categoryId() {
        return categoryId;
    }
}
