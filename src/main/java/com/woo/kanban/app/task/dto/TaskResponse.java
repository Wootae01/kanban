package com.woo.kanban.app.task.dto;

import com.woo.kanban.app.task.Task;
import com.woo.kanban.app.task.TaskPriority;
import com.woo.kanban.app.task.TaskStatus;

import java.time.LocalDate;

public record TaskResponse (
        Long id,
        String title,
        String category,
        String content,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate,
        Long assigneeId,
        Long createdBy
) {
    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getCategory(),
                task.getContent(),
                task.getStatus(),
                task.getPriority(),
                task.getDueDate(),
                task.getAssigneeId(),
                task.getCreatedBy()
        );
    }
}
