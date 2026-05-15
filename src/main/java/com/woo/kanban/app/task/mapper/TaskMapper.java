package com.woo.kanban.app.task.mapper;

import com.woo.kanban.app.task.Task;
import com.woo.kanban.app.task.TaskStatus;
import com.woo.kanban.app.task.dto.TaskDetailResponse;
import com.woo.kanban.app.task.dto.TaskResponse;
import com.woo.kanban.app.task.dto.TaskUpdateRequest;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface TaskMapper {
    void insert(Task task);

    List<TaskResponse> findTasksByWorkspaceId(Long workspaceId);

    Optional<TaskDetailResponse> findTaskDetailById(Long taskId);

    Optional<Task> findById(Long taskId);

    boolean existById(Long taskId);

    void update(@Param("taskId") Long taskId, @Param("request") TaskUpdateRequest request);

    void updateStatus(@Param("taskId") Long taskId, @Param("status") TaskStatus status);

    void updateAssignee(@Param("taskId") Long taskId, @Param("assigneeId") Long assigneeId);

    void delete(Long taskId);
}
