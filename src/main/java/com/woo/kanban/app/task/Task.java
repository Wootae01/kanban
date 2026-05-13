package com.woo.kanban.app.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Task {
    private Long id;
    private Long workspaceId;
    private String title;
    private String content;
    private TaskStatus status;
    private TaskPriority priority;
    private String category;
    private LocalDate dueDate;
    private Long assigneeId;
    private Long createdBy;
    private LocalDateTime createdAt;
}
