package com.woo.kanban.app.task.controller;

import com.woo.kanban.app.task.dto.*;
import com.woo.kanban.app.task.service.TaskService;
import com.woo.kanban.app.user.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces/{workspaceId}/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // task 생성
    @PostMapping
    public ResponseEntity<Void> createTask(@PathVariable Long workspaceId,
                                           @RequestBody @Valid TaskCreateRequest dto,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        taskService.create(dto, workspaceId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    // task 목록 조회
    @GetMapping
    public ResponseEntity<List<TaskResponse>> getTaskList(@PathVariable Long workspaceId,
                                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<TaskResponse> result = taskService.findTasksByWorkspaceId(workspaceId, userDetails.getId());
        return ResponseEntity.ok(result);
    }

    // task 상세 조회
    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDetailResponse> getTaskDetail(@PathVariable Long workspaceId,
                                                            @PathVariable Long taskId,
                                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        TaskDetailResponse result = taskService.findTaskDetailById(taskId, workspaceId, userDetails.getId());
        return ResponseEntity.ok(result);
    }

    // task 수정
    @PutMapping("/{taskId}")
    public ResponseEntity<Void> updateTask(@PathVariable Long workspaceId,
                                           @PathVariable Long taskId,
                                           @RequestBody @Valid TaskUpdateRequest dto,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        taskService.update(taskId, userDetails.getId(), workspaceId, dto);
        return ResponseEntity.ok().build();
    }

    // task 삭제
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long workspaceId,
                                           @PathVariable Long taskId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        taskService.delete(taskId, workspaceId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    // task status 변경
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long workspaceId,
                                             @PathVariable Long taskId,
                                             @RequestBody @Valid TaskStatusUpdateRequest dto,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        taskService.updateStatus(taskId, userDetails.getId(), workspaceId, dto.status());
        return ResponseEntity.ok().build();
    }

    // task 담당자 변경
    @PatchMapping("/{taskId}/assignee")
    public ResponseEntity<Void> updateAssignee(@PathVariable Long workspaceId,
                                               @PathVariable Long taskId,
                                               @RequestBody @Valid TaskAssigneeUpdateRequest dto,
                                               @AuthenticationPrincipal CustomUserDetails userDetails) {
        taskService.updateAssignee(taskId, userDetails.getId(), workspaceId, dto.assigneeId());
        return ResponseEntity.ok().build();
    }
}
