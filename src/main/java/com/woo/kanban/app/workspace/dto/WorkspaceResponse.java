package com.woo.kanban.app.workspace.dto;

// workspace 목록 조회 시 사용
public record WorkspaceResponse (Long id, String name, Integer memberCount, Integer taskCount){}
