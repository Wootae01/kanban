package com.woo.kanban.app.workspace.service;

import com.woo.kanban.app.workspace.MemberRole;
import com.woo.kanban.app.workspace.WorkspaceMember;
import com.woo.kanban.app.workspace.mapper.WorkspaceMemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WorkspacePermissionChecker {

    private final WorkspaceMemberMapper workspaceMemberMapper;

    public void checkMember(Long workspaceId, Long userId) {
        workspaceMemberMapper.findByIdAndUserId(workspaceId, userId)
                .orElseThrow(() -> new AccessDeniedException("워크스페이스 멤버가 아닙니다."));
    }

    public void checkAdmin(Long workspaceId, Long userId) {
        WorkspaceMember member = workspaceMemberMapper.findByIdAndUserId(workspaceId, userId)
                .orElseThrow(() -> new AccessDeniedException("워크스페이스 멤버가 아닙니다."));
        if (!member.getRole().equals(MemberRole.ADMIN))
            throw new AccessDeniedException("관리자만 가능합니다.");
    }

}
