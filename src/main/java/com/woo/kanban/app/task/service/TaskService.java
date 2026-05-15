package com.woo.kanban.app.task.service;

import com.woo.kanban.app.task.Task;
import com.woo.kanban.app.task.TaskStatus;
import com.woo.kanban.app.task.dto.TaskCreateRequest;
import com.woo.kanban.app.task.dto.TaskDetailResponse;
import com.woo.kanban.app.task.dto.TaskResponse;
import com.woo.kanban.app.task.dto.TaskUpdateRequest;
import com.woo.kanban.app.task.mapper.TaskMapper;
import com.woo.kanban.app.workspace.service.WorkspacePermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskMapper taskMapper;
    private final WorkspacePermissionChecker permissionChecker;

    // task 생성 - 멤버면 누구나
    public Task create(TaskCreateRequest request, Long workspaceId, Long createdBy) {
        permissionChecker.checkMember(workspaceId, createdBy);
        Task task = request.toEntity(workspaceId, createdBy);
        taskMapper.insert(task);
        return task;
    }

    // task 목록 조회 - 멤버면 누구나
    public List<TaskResponse> findTasksByWorkspaceId(Long workspaceId, Long userId) {
        permissionChecker.checkMember(workspaceId, userId);
        return taskMapper.findTasksByWorkspaceId(workspaceId);
    }

    // task 상세 조회 - 멤버면 누구나
    public TaskDetailResponse findTaskDetailById(Long taskId, Long workspaceId, Long userId) {
        permissionChecker.checkMember(workspaceId, userId);
        return taskMapper.findTaskDetailById(taskId)
                .orElseThrow(() -> new NoSuchElementException("task를 찾을 수 없습니다."));
    }

    // task 수정 - ADMIN만
    public void update(Long taskId, Long userId, Long workspaceId, TaskUpdateRequest dto) {

        permissionChecker.checkAdmin(workspaceId, userId);
        if(!taskMapper.existById(taskId)) throw new NoSuchElementException("task를 찾을 수 없습니다.");

        taskMapper.update(taskId, dto);
    }

    // task 상태 변경 - 멤버면 누구나
    public void updateStatus(Long taskId, Long userId, Long workspaceId, TaskStatus status) {
        permissionChecker.checkMember(workspaceId, userId);
        if(!taskMapper.existById(taskId)) throw new NoSuchElementException("task를 찾을 수 없습니다.");

        taskMapper.updateStatus(taskId, status);
    }

    // task 담당자 변경 - ADMIN 또는 셀프
    public void updateAssignee(Long taskId, Long userId, Long workspaceId, Long assigneeId) {
        boolean isSelf = userId.equals(assigneeId);
        if (!isSelf) {
            permissionChecker.checkAdmin(workspaceId, userId);
        }
        if(!taskMapper.existById(taskId)) throw new NoSuchElementException("task를 찾을 수 없습니다.");

        taskMapper.updateAssignee(taskId, assigneeId);
    }

    // task 삭제 - ADMIN만
    public void delete(Long taskId, Long workspaceId, Long userId) {
        permissionChecker.checkAdmin(workspaceId, userId);
        if(!taskMapper.existById(taskId)) throw new NoSuchElementException("task를 찾을 수 없습니다.");

        taskMapper.delete(taskId);
    }
}
