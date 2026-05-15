package com.woo.kanban.app.task.dto;

import com.woo.kanban.app.task.TaskPriority;
import com.woo.kanban.app.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskUpdateRequest(
        @NotBlank String title,
        String content,
        @NotNull TaskStatus status,
        @NotNull TaskPriority priority,
        Long assigneeId,
        LocalDate dueDate,
        Long categoryId
) {
}
