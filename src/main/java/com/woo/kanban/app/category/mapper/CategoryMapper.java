package com.woo.kanban.app.category.mapper;

import com.woo.kanban.app.category.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CategoryMapper {

    void insert(Category category);

    boolean existsByNameAndWorkspaceId(@Param("workspaceId")Long workspaceId, @Param("name") String name);

    List<Category> findAll(Long workspaceId);

    void delete(Long categoryId);
}
