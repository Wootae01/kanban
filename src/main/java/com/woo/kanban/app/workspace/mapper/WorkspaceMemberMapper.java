package com.woo.kanban.app.workspace.mapper;

import com.woo.kanban.app.workspace.WorkspaceMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Optional;

@Mapper
public interface WorkspaceMemberMapper {

    void insert(WorkspaceMember workspaceMember);

    Optional<WorkspaceMember> findByIdAndUserId(@Param("workspaceId") Long workspaceId,
                                                @Param("userId") Long userId);

}
