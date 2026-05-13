package com.woo.kanban.app.task;

public enum TaskPriority {
    LOW("낮음"),
    MEDIUM("보통"),
    HIGH("높음");

    public final String label;

    TaskPriority(String label) {
        this.label = label;
    }
}
