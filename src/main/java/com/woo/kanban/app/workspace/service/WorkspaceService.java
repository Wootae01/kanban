package com.woo.kanban.app.workspace.service;

import com.woo.kanban.app.workspace.MemberRole;
import com.woo.kanban.app.workspace.Workspace;
import com.woo.kanban.app.workspace.WorkspaceMember;
import com.woo.kanban.app.workspace.dto.WorkspaceCreateRequest;
import com.woo.kanban.app.workspace.dto.WorkspaceDetailResponse;
import com.woo.kanban.app.workspace.dto.WorkspaceResponse;
import com.woo.kanban.app.workspace.dto.WorkspaceUpdateRequest;
import com.woo.kanban.app.workspace.mapper.WorkspaceMapper;
import com.woo.kanban.app.workspace.mapper.WorkspaceMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceMapper workspaceMapper;
    private final WorkspaceMemberMapper workspaceMemberMapper;

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

    // update
    @Transactional
    public void update(Long workspaceId, Long userId, WorkspaceUpdateRequest request) {

        validateAdminAceess(workspaceId, userId, "관리자만 수정할 수 있습니다.");

        // 수정
        workspaceMapper.update(workspaceId, request.name());
    }

    @Transactional
    public void delete(Long workspaceId, Long userId) {
        // workspace 존재 확인
        validateAdminAceess(workspaceId, userId, "관리자만 삭제할 수 있습니다.");

        workspaceMapper.delete(workspaceId);
    }

    private void validateAdminAceess(Long workspaceId, Long userId, String errorMessage) {
        // workspace 존재 확인
        workspaceMapper.findById(workspaceId)
                .orElseThrow(() -> new NoSuchElementException("workspace가 존재하지 않습니다."));


        WorkspaceMember member = workspaceMemberMapper.findByIdAndUserId(workspaceId, userId)
                .orElseThrow(() -> new AccessDeniedException("workspace 멤버가 아닙니다."));

        // 관리자만 수정 가능
        if (!member.getRole().equals(MemberRole.ADMIN)) {
            throw new AccessDeniedException(errorMessage);
        }
    }
}
