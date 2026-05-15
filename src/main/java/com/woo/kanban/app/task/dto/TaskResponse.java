package com.woo.kanban.app.task.dto;

import com.woo.kanban.app.task.TaskPriority;
import com.woo.kanban.app.task.TaskStatus;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        String title,
        String categoryName,
        String content,
        TaskStatus status,
        TaskPriority priority,
        LocalDate dueDate,
        Long assigneeId,
        Long createdBy
) {
}
