package com.woo.kanban.app.workspace.dto;


import jakarta.validation.constraints.NotBlank;

public record WorkspaceCreateRequest(@NotBlank String name) {

}
