package com.woo.kanban.app.task.dto;

import com.woo.kanban.app.task.Task;
import com.woo.kanban.app.task.TaskPriority;
import com.woo.kanban.app.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record TaskCreateRequest(
        @NotBlank String title,
        String content,
        @NotNull TaskStatus status,
        @NotNull TaskPriority priority,
        LocalDate dueDate,
        Long assigneeId,
        Long categoryId
) {
    public Task toEntity(Long workspaceId, Long createdBy) {
        Task task = new Task();
        task.setTitle(this.title());
        task.setContent(this.content());
        task.setStatus(this.status());
        task.setPriority(this.priority());
        task.setDueDate(this.dueDate());
        task.setWorkspaceId(workspaceId);
        task.setAssigneeId(this.assigneeId());
        task.setCreatedBy(createdBy);
        task.setCategoryId(categoryId);
        return task;
    }
}

