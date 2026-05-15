package com.woo.kanban.app.task.dto;

import jakarta.validation.constraints.NotNull;

public record TaskAssigneeUpdateRequest(
        @NotNull Long assigneeId
) {}
