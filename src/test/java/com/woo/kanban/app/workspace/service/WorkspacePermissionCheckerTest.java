package com.woo.kanban.app.workspace.service;

import com.woo.kanban.app.workspace.MemberRole;
import com.woo.kanban.app.workspace.Workspace;
import com.woo.kanban.app.workspace.WorkspaceMember;
import com.woo.kanban.app.workspace.mapper.WorkspaceMemberMapper;
import com.woo.kanban.app.workspace.mapper.WorkspaceMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspacePermissionCheckerTest {

    @InjectMocks
    WorkspacePermissionChecker permissionChecker;

    @Mock
    WorkspaceMapper workspaceMapper;

    @Mock
    WorkspaceMemberMapper workspaceMemberMapper;

    private static final Long WORKSPACE_ID = 10L;
    private static final Long USER_ID = 1L;

    @Nested
    @DisplayName("멤버 확인")
    class CheckMember {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            when(workspaceMapper.findById(WORKSPACE_ID)).thenReturn(Optional.of(new Workspace()));
            when(workspaceMemberMapper.findByIdAndUserId(WORKSPACE_ID, USER_ID))
                    .thenReturn(Optional.of(new WorkspaceMember()));

            // when & then
            assertThatCode(() -> permissionChecker.checkMember(WORKSPACE_ID, USER_ID))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("실패 - workspace 없음")
        void workspaceNotFound() {
            // given
            when(workspaceMapper.findById(WORKSPACE_ID)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> permissionChecker.checkMember(WORKSPACE_ID, USER_ID))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("실패 - 멤버 아님")
        void notMember() {
            // given
            when(workspaceMapper.findById(WORKSPACE_ID)).thenReturn(Optional.of(new Workspace()));
            when(workspaceMemberMapper.findByIdAndUserId(WORKSPACE_ID, USER_ID))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> permissionChecker.checkMember(WORKSPACE_ID, USER_ID))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("워크스페이스 멤버가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("관리자 확인")
    class CheckAdmin {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            WorkspaceMember admin = new WorkspaceMember();
            admin.setRole(MemberRole.ADMIN);
            when(workspaceMapper.findById(WORKSPACE_ID)).thenReturn(Optional.of(new Workspace()));
            when(workspaceMemberMapper.findByIdAndUserId(WORKSPACE_ID, USER_ID))
                    .thenReturn(Optional.of(admin));

            // when & then
            assertThatCode(() -> permissionChecker.checkAdmin(WORKSPACE_ID, USER_ID))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("실패 - workspace 없음")
        void workspaceNotFound() {
            // given
            when(workspaceMapper.findById(WORKSPACE_ID)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> permissionChecker.checkAdmin(WORKSPACE_ID, USER_ID))
                    .isInstanceOf(NoSuchElementException.class);
        }

        @Test
        @DisplayName("실패 - 멤버 아님")
        void notMember() {
            // given
            when(workspaceMapper.findById(WORKSPACE_ID)).thenReturn(Optional.of(new Workspace()));
            when(workspaceMemberMapper.findByIdAndUserId(WORKSPACE_ID, USER_ID))
                    .thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> permissionChecker.checkAdmin(WORKSPACE_ID, USER_ID))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("워크스페이스 멤버가 아닙니다.");
        }

        @Test
        @DisplayName("실패 - 관리자 아님")
        void notAdmin() {
            // given
            WorkspaceMember member = new WorkspaceMember();
            member.setRole(MemberRole.MEMBER);
            when(workspaceMapper.findById(WORKSPACE_ID)).thenReturn(Optional.of(new Workspace()));
            when(workspaceMemberMapper.findByIdAndUserId(WORKSPACE_ID, USER_ID))
                    .thenReturn(Optional.of(member));

            // when & then
            assertThatThrownBy(() -> permissionChecker.checkAdmin(WORKSPACE_ID, USER_ID))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("관리자만 가능합니다.");
        }
    }
}
