package com.woo.kanban.app.category;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Category {
    private Long id;
    private Long workspaceId;
    private String name;
    private LocalDateTime createdAt;
}
