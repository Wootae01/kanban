package com.woo.kanban.app.category.service;

import com.woo.kanban.app.category.Category;
import com.woo.kanban.app.category.dto.CategoryCreateRequest;
import com.woo.kanban.app.category.dto.CategoryResponse;
import com.woo.kanban.app.category.mapper.CategoryMapper;
import com.woo.kanban.app.workspace.service.WorkspacePermissionChecker;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryMapper categoryMapper;
    private final WorkspacePermissionChecker permissionChecker;

    public Category create(CategoryCreateRequest request, Long workspaceId, Long userId) {

        // admin 체크
        permissionChecker.checkAdmin(workspaceId, userId);

        // 중복 이름 체크
        boolean b = categoryMapper.existsByNameAndWorkspaceId(workspaceId, request.name());
        if (b) {
            throw new IllegalArgumentException("이미 존재하는 카테고리 입니다.");
        }
        Category category = request.toEntity(workspaceId);
        categoryMapper.insert(category);
        return category;
    }

    public List<CategoryResponse> findAll(Long workspaceId, Long userId) {

        permissionChecker.checkMember(workspaceId, userId);
        return categoryMapper.findAll(workspaceId).stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .toList();
    }

    public void delete(Long workspaceId, Long categoryId, Long userId) {
        permissionChecker.checkAdmin(workspaceId, userId);
        categoryMapper.delete(categoryId);
    }
}
