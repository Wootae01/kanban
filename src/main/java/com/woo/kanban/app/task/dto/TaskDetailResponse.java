package com.woo.kanban.app.task.dto;

import com.woo.kanban.app.task.TaskPriority;
import com.woo.kanban.app.task.TaskStatus;

import java.time.LocalDate;

public record TaskDetailResponse (
        Long id,
        String title,
        String content,
        TaskStatus status,
        TaskPriority priority,
        String assigneeName,
        LocalDate dueDate,
        String categoryName
) {
}
