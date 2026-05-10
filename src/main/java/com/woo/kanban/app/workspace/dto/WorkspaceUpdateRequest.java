package com.woo.kanban.app.workspace.dto;

import jakarta.validation.constraints.NotBlank;

public record WorkspaceUpdateRequest(@NotBlank String name) {
}
