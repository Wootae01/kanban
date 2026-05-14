package com.woo.kanban.app.workspace.dto;

import com.woo.kanban.app.workspace.MemberRole;

public record MemberResponse (Long userId,
                              String name,
                              MemberRole role) {

}
