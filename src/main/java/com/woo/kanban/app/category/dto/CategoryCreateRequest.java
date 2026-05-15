package com.woo.kanban.app.category.dto;

import com.woo.kanban.app.category.Category;
import jakarta.validation.constraints.NotBlank;

public record CategoryCreateRequest(@NotBlank String name) {

    public Category toEntity(Long workspaceId) {
        Category category = new Category();
        category.setWorkspaceId(workspaceId);
        category.setName(this.name());
        return category;
    }
}

