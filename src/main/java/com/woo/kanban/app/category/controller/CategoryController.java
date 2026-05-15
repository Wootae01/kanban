package com.woo.kanban.app.category.controller;

import com.woo.kanban.app.category.dto.CategoryCreateRequest;
import com.woo.kanban.app.category.dto.CategoryResponse;
import com.woo.kanban.app.category.service.CategoryService;
import com.woo.kanban.app.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/workspaces/{workspaceId}/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;


    // 카테고리 생성
    @PostMapping
    public ResponseEntity<Void> createCategory(@AuthenticationPrincipal CustomUserDetails userDetails,
                                               @RequestBody @Valid CategoryCreateRequest dto,
                                               @PathVariable Long workspaceId) {

        categoryService.create(dto, workspaceId, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    // 카테고리 목록 조회
    @GetMapping
    public ResponseEntity<List<CategoryResponse>> findCategories(@PathVariable Long workspaceId,
                                                                 @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<CategoryResponse> result = categoryService.findAll(workspaceId, userDetails.getId());

        return ResponseEntity.ok(result);
    }

    // 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long workspaceId,
                                              @PathVariable Long categoryId,
                                              @AuthenticationPrincipal CustomUserDetails userDetails) {

        categoryService.delete(workspaceId, categoryId, userDetails.getId());
        return ResponseEntity.ok().build();
    }
}
