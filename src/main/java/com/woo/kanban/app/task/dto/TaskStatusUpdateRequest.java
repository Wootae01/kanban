package com.woo.kanban.app.task.dto;

import com.woo.kanban.app.task.TaskStatus;
import jakarta.validation.constraints.NotNull;

public record TaskStatusUpdateRequest(
        @NotNull TaskStatus status
) {}
