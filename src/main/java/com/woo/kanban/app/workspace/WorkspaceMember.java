package com.woo.kanban.app.workspace;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkspaceMember {
    private Long id;
    private Long workspaceId;
    private Long userId;
    private MemberRole role;
}
