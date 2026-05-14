package com.woo.kanban.app.workspace.service;

import com.woo.kanban.app.workspace.MemberRole;
import com.woo.kanban.app.workspace.WorkspaceMember;
import com.woo.kanban.app.workspace.mapper.WorkspaceMemberMapper;
import com.woo.kanban.app.workspace.mapper.WorkspaceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.NoSuchElementException;

@Component
@RequiredArgsConstructor
public class WorkspacePermissionChecker {

    private final WorkspaceMapper workspaceMapper;
    private final WorkspaceMemberMapper workspaceMemberMapper;

    public void checkMember(Long workspaceId, Long userId) {
        workspaceMapper.findById(workspaceId)
                .orElseThrow(() -> new NoSuchElementException("workspace가 존재하지 않습니다."));
        workspaceMemberMapper.findByIdAndUserId(workspaceId, userId)
                .orElseThrow(() -> new AccessDeniedException("워크스페이스 멤버가 아닙니다."));
    }

    public void checkAdmin(Long workspaceId, Long userId) {
        workspaceMapper.findById(workspaceId)
                .orElseThrow(() -> new NoSuchElementException("workspace가 존재하지 않습니다."));
        WorkspaceMember member = workspaceMemberMapper.findByIdAndUserId(workspaceId, userId)
                .orElseThrow(() -> new AccessDeniedException("워크스페이스 멤버가 아닙니다."));
        if (!member.getRole().equals(MemberRole.ADMIN))
            throw new AccessDeniedException("관리자만 가능합니다.");
    }

}
