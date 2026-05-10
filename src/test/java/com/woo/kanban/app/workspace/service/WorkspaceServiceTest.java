package com.woo.kanban.app.workspace.service;

import com.woo.kanban.app.workspace.Workspace;
import com.woo.kanban.app.workspace.WorkspaceFixture;
import com.woo.kanban.app.workspace.dto.WorkspaceCreateRequest;
import com.woo.kanban.app.workspace.dto.WorkspaceDetailResponse;
import com.woo.kanban.app.workspace.dto.WorkspaceResponse;
import com.woo.kanban.app.workspace.dto.WorkspaceUpdateRequest;
import com.woo.kanban.app.workspace.mapper.WorkspaceMapper;
import com.woo.kanban.app.workspace.mapper.WorkspaceMemberMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WorkspaceServiceTest {

    @InjectMocks
    WorkspaceService workspaceService;

    @Mock
    WorkspaceMapper workspaceMapper;

    @Mock
    WorkspaceMemberMapper workspaceMemberMapper;

    @Test
    @DisplayName("workspace_생성")
    public void createWorkspace() {

        // given
        WorkspaceCreateRequest request = new WorkspaceCreateRequest("name");
        Long userId = 1L;
        doAnswer(invocation -> {
            Workspace w = invocation.getArgument(0);
            w.setId(10L);
            return null;
        }).when(workspaceMapper).insert(any(Workspace.class));

        // when
        Workspace workspace = workspaceService.create(request, userId);

        // then
        verify(workspaceMapper).insert(argThat(w ->
                w.getName().equals("name") &&
                        w.getCreatedBy().equals(1L)
        ));

        verify(workspaceMemberMapper).insert(argThat(member ->
                member.getWorkspaceId().equals(workspace.getId())));
    }

    @Test
    @DisplayName("workspace_목록_조회")
    public void readWorkspace() {

        // given
        Long userId = 1L;
        List<WorkspaceResponse> mockList = List.of(
                new WorkspaceResponse(1L, "workspace1", 3, 5),
                new WorkspaceResponse(2L, "workspace2", 1, 2)
        );
        when(workspaceMapper.findWorkspacesByUserId(userId)).thenReturn(mockList);


        // when
        List<WorkspaceResponse> result = workspaceService.findWorkspaceList(userId);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.getFirst().name()).isEqualTo("workspace1");
    }

    @Test
    @DisplayName("workspace_상세조회")
    public void findWorkspaceById() {

        // given
        Long id = 1L;
        String name = "workspace test";
        Workspace mock = new Workspace();
        mock.setId(id);
        mock.setName("workspace test");
        when(workspaceMapper.findById(id)).thenReturn(Optional.of(mock));

        // when
        WorkspaceDetailResponse dto = workspaceService.findById(id);

        // then
        assertThat(dto.id()).isEqualTo(id);
        assertThat(dto.name()).isEqualTo(name);
    }

    @Test
    @DisplayName("workspace_상세조회_실패_없는id")
    void findWorkspaceByIdFail() {
        // given
        when(workspaceMapper.findById(999L)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> workspaceService.findById(999L))
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("workspace_수정_성공")
    void updateSuccess() {
        // given
        WorkspaceUpdateRequest request = new WorkspaceUpdateRequest("newName");

        when(workspaceMapper.findById(WorkspaceFixture.ID)).thenReturn(Optional.of(WorkspaceFixture.workspace()));
        when(workspaceMemberMapper.findByIdAndUserId(WorkspaceFixture.ID, WorkspaceFixture.USER_ID)).thenReturn(Optional.of(WorkspaceFixture.adminMember()));

        // when
        workspaceService.update(WorkspaceFixture.ID, WorkspaceFixture.USER_ID, request);

        // then
        verify(workspaceMapper).update(WorkspaceFixture.ID, "newName");
    }

    @Test
    @DisplayName("workspace_수정_실패_권한없음")
    void updateFailNotAdmin() {

        // given
        WorkspaceUpdateRequest request = new WorkspaceUpdateRequest("testName");

        when(workspaceMapper.findById(WorkspaceFixture.ID)).thenReturn(Optional.of(WorkspaceFixture.workspace()));
        when(workspaceMemberMapper.findByIdAndUserId(WorkspaceFixture.ID, WorkspaceFixture.USER_ID)).thenReturn(Optional.of(WorkspaceFixture.nonAdminMember()));

        // when & then
        assertThatThrownBy(() -> workspaceService.update(WorkspaceFixture.ID, WorkspaceFixture.USER_ID, request))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("workspace_수정_실패_멤버아님")
    void updateFailNotMember() {

        // given
        WorkspaceUpdateRequest request = new WorkspaceUpdateRequest("testName");

        when(workspaceMapper.findById(WorkspaceFixture.ID)).thenReturn(Optional.of(WorkspaceFixture.workspace()));
        when(workspaceMemberMapper.findByIdAndUserId(WorkspaceFixture.ID, WorkspaceFixture.USER_ID)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> workspaceService.update(WorkspaceFixture.ID, WorkspaceFixture.USER_ID, request))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("workspace_삭제_성공")
    void delete() {

        // given
        when(workspaceMapper.findById(WorkspaceFixture.ID)).thenReturn(Optional.of(WorkspaceFixture.workspace()));
        when(workspaceMemberMapper.findByIdAndUserId(WorkspaceFixture.ID, WorkspaceFixture.USER_ID)).thenReturn(Optional.of(WorkspaceFixture.adminMember()));

        // when
        workspaceService.delete(WorkspaceFixture.ID, WorkspaceFixture.USER_ID);

        // then
        verify(workspaceMapper).delete(WorkspaceFixture.ID);
    }

    @Test
    @DisplayName("workspace_삭제_실패_권한없음")
    void deleteFailNotAdmin() {

        // given
        when(workspaceMapper.findById(WorkspaceFixture.ID)).thenReturn(Optional.of(WorkspaceFixture.workspace()));
        when(workspaceMemberMapper.findByIdAndUserId(WorkspaceFixture.ID, WorkspaceFixture.USER_ID)).thenReturn(Optional.of(WorkspaceFixture.nonAdminMember()));

        // when & then
        assertThatThrownBy(() -> workspaceService.delete(WorkspaceFixture.ID, WorkspaceFixture.USER_ID))
                .isInstanceOf(AccessDeniedException.class);
    }

    @Test
    @DisplayName("workspace_삭제_실패_멤버아님")
    void deleteFailNotMember() {

        // given
        when(workspaceMapper.findById(WorkspaceFixture.ID)).thenReturn(Optional.of(WorkspaceFixture.workspace()));
        when(workspaceMemberMapper.findByIdAndUserId(WorkspaceFixture.ID, WorkspaceFixture.USER_ID)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> workspaceService.delete(WorkspaceFixture.ID, WorkspaceFixture.USER_ID))
                .isInstanceOf(AccessDeniedException.class);
    }

}