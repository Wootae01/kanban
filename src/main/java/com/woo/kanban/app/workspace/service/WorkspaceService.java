package com.woo.kanban.app.workspace.service;

import com.woo.kanban.app.workspace.MemberRole;
import com.woo.kanban.app.workspace.Workspace;
import com.woo.kanban.app.workspace.WorkspaceMember;
import com.woo.kanban.app.workspace.dto.*;
import com.woo.kanban.app.workspace.mapper.WorkspaceMapper;
import com.woo.kanban.app.workspace.mapper.WorkspaceMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceMapper workspaceMapper;
    private final WorkspaceMemberMapper workspaceMemberMapper;
    private final WorkspacePermissionChecker permissionChecker;

    @Transactional
    public Workspace create(WorkspaceCreateRequest request, Long userId) {

        Workspace workspace = new Workspace();
        workspace.setName(request.name());
        workspace.setCreatedBy(userId);
        workspaceMapper.insert(workspace);

        WorkspaceMember member = new WorkspaceMember();
        member.setUserId(userId);
        member.setRole(MemberRole.ADMIN);
        member.setWorkspaceId(workspace.getId());
        workspaceMemberMapper.insert(member);

        return workspace;
    }


    // workspace 목록 조회
    public List<WorkspaceResponse> findWorkspaceList(Long userId) {
        return workspaceMapper.findWorkspacesByUserId(userId);
    }

    // workspace 상세 조회
    public WorkspaceDetailResponse findById(Long id) {
        Workspace workspace = workspaceMapper.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 workspace는 존재하지 않습니다."));

        return new WorkspaceDetailResponse(workspace.getId(), workspace.getName());

    }

    // workspace 멤버 조회
    public List<MemberResponse> getMemberList(Long workspaceId, Long userId) {
        permissionChecker.checkMember(workspaceId, userId);
        return workspaceMemberMapper.findMembersById(workspaceId);
    }

    // update
    @Transactional
    public void update(Long workspaceId, Long userId, WorkspaceUpdateRequest request) {
        permissionChecker.checkAdmin(workspaceId, userId);
        workspaceMapper.update(workspaceId, request.name());
    }

    // workspace 삭제
    @Transactional
    public void delete(Long workspaceId, Long userId) {
        permissionChecker.checkAdmin(workspaceId, userId);
        workspaceMapper.delete(workspaceId);
    }

    // workspace member 삭제
    @Transactional
    public void deleteMember(Long workspaceId, Long memberId, Long userId) {
        permissionChecker.checkAdmin(workspaceId, userId);
        workspaceMemberMapper.deleteMember(workspaceId, memberId);
    }
}
