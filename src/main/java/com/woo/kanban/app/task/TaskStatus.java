package com.woo.kanban.app.task;

public enum TaskStatus {
    TODO("할 일"),
    IN_PROGRESS("진행 중"),
    DONE("완료");

    public final String label;

    TaskStatus(String label) {
        this.label = label;
    }
}
