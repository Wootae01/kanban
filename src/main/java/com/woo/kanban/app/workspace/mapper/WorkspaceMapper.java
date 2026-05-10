package com.woo.kanban.app.workspace.mapper;

import com.woo.kanban.app.workspace.Workspace;
import com.woo.kanban.app.workspace.dto.WorkspaceResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface WorkspaceMapper {

    void insert(Workspace workspace);

    List<WorkspaceResponse> findWorkspacesByUserId(Long userId);

    Optional<Workspace> findById(Long id);

    void update(@Param("id") Long id, @Param("name") String name);

    void delete(Long id);

}
