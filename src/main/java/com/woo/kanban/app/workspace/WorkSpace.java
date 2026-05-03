package com.woo.kanban.app.workspace;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class WorkSpace {
    private Long id;
    private String name;
    private LocalDateTime createdAt;

    private Long createdBy;
}
