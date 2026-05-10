package com.woo.kanban.app.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Workspace {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    private Long createdBy; // userId
}
