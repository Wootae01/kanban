package com.woo.kanban.app.task.service;

import com.woo.kanban.app.task.TaskPriority;
import com.woo.kanban.app.task.TaskStatus;
import com.woo.kanban.app.task.dto.TaskCreateRequest;
import com.woo.kanban.app.task.dto.TaskDetailResponse;
import com.woo.kanban.app.task.dto.TaskResponse;
import com.woo.kanban.app.task.dto.TaskUpdateRequest;
import com.woo.kanban.app.task.mapper.TaskMapper;
import com.woo.kanban.app.workspace.service.WorkspacePermissionChecker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @InjectMocks
    TaskService taskService;

    @Mock
    TaskMapper taskMapper;

    @Mock
    WorkspacePermissionChecker permissionChecker;

    @Nested
    @DisplayName("task 생성")
    class Create {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            TaskCreateRequest request = new TaskCreateRequest(
                    "title", "content",
                    TaskStatus.TODO, TaskPriority.LOW,
                    LocalDate.now(), 1L, null);

            // when
            taskService.create(request, 10L, 2L);

            // then
            verify(taskMapper).insert(argThat(t ->
                    t.getWorkspaceId().equals(10L) &&
                            t.getCreatedBy().equals(2L) &&
                            t.getTitle().equals("title")
            ));
        }

        @Test
        @DisplayName("실패 - 멤버 아님")
        void notMember() {
            // given
            TaskCreateRequest request = new TaskCreateRequest(
                    "title", "content",
                    TaskStatus.TODO, TaskPriority.LOW,
                    LocalDate.now(), 1L, null);

            doThrow(new AccessDeniedException("워크스페이스 멤버가 아닙니다."))
                    .when(permissionChecker).checkMember(10L, 2L);

            // when & then
            assertThatThrownBy(() -> taskService.create(request, 10L, 2L))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("워크스페이스 멤버가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("task 목록 조회")
    class FindList {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            TaskResponse taskResponse = new TaskResponse(
                    1L, "title", null, "content",
                    TaskStatus.TODO, TaskPriority.LOW,
                    null, null, 2L);

            when(taskMapper.findTasksByWorkspaceId(10L)).thenReturn(List.of(taskResponse));

            // when
            List<TaskResponse> result = taskService.findTasksByWorkspaceId(10L, 2L);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.getFirst().title()).isEqualTo("title");
            assertThat(result.getFirst().status()).isEqualTo(TaskStatus.TODO);
            verify(taskMapper).findTasksByWorkspaceId(10L);
        }

        @Test
        @DisplayName("실패 - 멤버 아님")
        void notMember() {
            // given
            doThrow(new AccessDeniedException("워크스페이스 멤버가 아닙니다."))
                    .when(permissionChecker).checkMember(10L, 2L);

            // when & then
            assertThatThrownBy(() -> taskService.findTasksByWorkspaceId(10L, 2L))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("워크스페이스 멤버가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("task 상세 조회")
    class FindDetail {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long taskId = 1L;
            TaskDetailResponse data = new TaskDetailResponse(
                    1L, "title", "content",
                    TaskStatus.TODO, TaskPriority.HIGH, "name", LocalDate.now(), "백엔드");

            when(taskMapper.findTaskDetailById(taskId)).thenReturn(Optional.of(data));

            // when
            TaskDetailResponse result = taskService.findTaskDetailById(taskId, 10L, 2L);

            // then
            assertThat(result).isEqualTo(data);
            verify(taskMapper).findTaskDetailById(taskId);
        }

        @Test
        @DisplayName("실패 - task 없음")
        void notFound() {
            // given
            when(taskMapper.findTaskDetailById(999L)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> taskService.findTaskDetailById(999L, 10L, 2L))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("task를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("실패 - 멤버 아님")
        void notMember() {
            // given
            doThrow(new AccessDeniedException("워크스페이스 멤버가 아닙니다."))
                    .when(permissionChecker).checkMember(10L, 2L);

            // when & then
            assertThatThrownBy(() -> taskService.findTaskDetailById(1L, 10L, 2L))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("워크스페이스 멤버가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("task 수정")
    class Update {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long taskId = 1L;
            Long userId = 2L;
            TaskUpdateRequest dto = new TaskUpdateRequest("title", "content",
                    TaskStatus.TODO, TaskPriority.HIGH, 1L, LocalDate.now(), null);

            when(taskMapper.existById(taskId)).thenReturn(true);

            // when
            taskService.update(taskId, userId, 10L, dto);

            // then
            verify(taskMapper).update(taskId, dto);
        }

        @Test
        @DisplayName("실패 - task 없음")
        void notFound() {
            // given
            when(taskMapper.existById(999L)).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> taskService.update(999L, 2L, 10L, null))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("task를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("실패 - 멤버 아님")
        void notMember() {
            // given
            doThrow(new AccessDeniedException("워크스페이스 멤버가 아닙니다."))
                    .when(permissionChecker).checkAdmin(10L, 2L);

            // when & then
            assertThatThrownBy(() -> taskService.update(1L, 2L, 10L, null))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("워크스페이스 멤버가 아닙니다.");
        }

        @Test
        @DisplayName("실패 - 관리자 아님")
        void notAdmin() {
            // given
            doThrow(new AccessDeniedException("관리자만 가능합니다."))
                    .when(permissionChecker).checkAdmin(10L, 2L);

            // when & then
            assertThatThrownBy(() -> taskService.update(1L, 2L, 10L, null))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("관리자만 가능합니다.");
        }
    }

    @Nested
    @DisplayName("task 상태 변경")
    class UpdateStatus {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            when(taskMapper.existById(1L)).thenReturn(true);

            // when
            taskService.updateStatus(1L, 2L, 10L, TaskStatus.IN_PROGRESS);

            // then
            verify(taskMapper).updateStatus(1L, TaskStatus.IN_PROGRESS);
        }

        @Test
        @DisplayName("실패 - task 없음")
        void notFound() {
            // given
            when(taskMapper.existById(999L)).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> taskService.updateStatus(999L, 2L, 10L, TaskStatus.IN_PROGRESS))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("task를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("실패 - 멤버 아님")
        void notMember() {
            // given
            doThrow(new AccessDeniedException("워크스페이스 멤버가 아닙니다."))
                    .when(permissionChecker).checkMember(10L, 2L);

            // when & then
            assertThatThrownBy(() -> taskService.updateStatus(1L, 2L, 10L, TaskStatus.IN_PROGRESS))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("워크스페이스 멤버가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("task 담당자 변경")
    class UpdateAssignee {

        @Test
        @DisplayName("성공 - ADMIN")
        void successByAdmin() {
            // given
            when(taskMapper.existById(1L)).thenReturn(true);

            // when
            taskService.updateAssignee(1L, 2L, 10L, 3L);

            // then
            verify(taskMapper).updateAssignee(1L, 3L);
        }

        @Test
        @DisplayName("성공 - 셀프 배정")
        void successBySelf() {
            // given
            when(taskMapper.existById(1L)).thenReturn(true);

            // when
            taskService.updateAssignee(1L, 2L, 10L, 2L);

            // then
            verify(taskMapper).updateAssignee(1L, 2L);
            verify(permissionChecker, never()).checkAdmin(any(), any());
        }

        @Test
        @DisplayName("실패 - task 없음")
        void notFound() {
            // given
            when(taskMapper.existById(1L)).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> taskService.updateAssignee(1L, 2L, 10L, 3L))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("task를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("실패 - 권한 없음 (admin 아님, 셀프 아님)")
        void notAuthorized() {
            // given
            doThrow(new AccessDeniedException("관리자만 가능합니다."))
                    .when(permissionChecker).checkAdmin(10L, 2L);

            // when & then
            assertThatThrownBy(() -> taskService.updateAssignee(1L, 2L, 10L, 3L))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("관리자만 가능합니다.");
        }
    }

    @Nested
    @DisplayName("task 삭제")
    class Delete {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            when(taskMapper.existById(1L)).thenReturn(true);

            // when
            taskService.delete(1L, 10L, 2L);

            // then
            verify(taskMapper).delete(1L);
        }

        @Test
        @DisplayName("실패 - task 없음")
        void notFound() {
            // given
            when(taskMapper.existById(999L)).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> taskService.delete(999L, 10L, 2L))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessage("task를 찾을 수 없습니다.");
        }

        @Test
        @DisplayName("실패 - 멤버 아님")
        void notMember() {
            // given
            doThrow(new AccessDeniedException("워크스페이스 멤버가 아닙니다."))
                    .when(permissionChecker).checkAdmin(10L, 2L);

            // when & then
            assertThatThrownBy(() -> taskService.delete(1L, 10L, 2L))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("워크스페이스 멤버가 아닙니다.");
        }

        @Test
        @DisplayName("실패 - 관리자 아님")
        void notAdmin() {
            // given
            doThrow(new AccessDeniedException("관리자만 가능합니다."))
                    .when(permissionChecker).checkAdmin(10L, 2L);

            // when & then
            assertThatThrownBy(() -> taskService.delete(1L, 10L, 2L))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("관리자만 가능합니다.");
        }
    }
}
