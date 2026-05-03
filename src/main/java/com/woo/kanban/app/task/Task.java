package com.woo.kanban.app.task;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Task {
    private Long id;
    private String title;
    private String content;
    private TaskStatus status;
    private TaskPriority priority;
    private LocalDateTime dueDate;

    private Long workSpaceId;
    private Long assigneeId;
    private Long createdBy;

    private LocalDateTime createdAt;
}
