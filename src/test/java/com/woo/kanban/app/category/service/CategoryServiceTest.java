package com.woo.kanban.app.category.service;

import com.woo.kanban.app.category.Category;
import com.woo.kanban.app.category.dto.CategoryCreateRequest;
import com.woo.kanban.app.category.dto.CategoryResponse;
import com.woo.kanban.app.category.mapper.CategoryMapper;
import com.woo.kanban.app.workspace.service.WorkspacePermissionChecker;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @InjectMocks
    CategoryService categoryService;

    @Mock
    CategoryMapper categoryMapper;

    @Mock
    WorkspacePermissionChecker permissionChecker;


    @Nested
    @DisplayName("카테고리 생성")
    class Create {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            CategoryCreateRequest dto = new CategoryCreateRequest("name");
            Long workspaceId = 1L;
            Long userId = 3L;

            // when
            categoryService.create(dto, workspaceId, userId);

            // then
            verify(categoryMapper).existsByNameAndWorkspaceId(workspaceId, "name");
            verify(categoryMapper).insert(argThat(c ->
                    c.getWorkspaceId().equals(workspaceId) &&
                            c.getName().equals("name")
            ));
        }

        @Test
        @DisplayName("실패_중복 이름")
        void duplicateName() {
            // given
            CategoryCreateRequest dto = new CategoryCreateRequest("name");
            Long workspaceId = 1L;
            Long userId = 3L;

            when(categoryMapper.existsByNameAndWorkspaceId(workspaceId, "name")).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> categoryService.create(dto, workspaceId, userId))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("이미 존재하는 카테고리 입니다.");
        }

        @Test
        @DisplayName("실패 - 관리자 아님")
        void notAdmin() {
            // given
            CategoryCreateRequest dto = new CategoryCreateRequest("name");
            Long workspaceId = 1L;
            Long userId = 3L;

            doThrow(new AccessDeniedException("관리자만 가능합니다."))
                    .when(permissionChecker).checkAdmin(workspaceId, userId);

            // when & then
            assertThatThrownBy(() -> categoryService.create(dto, workspaceId, userId))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("관리자만 가능합니다.");
        }

    }

    @Nested
    @DisplayName("카테고리 목록 조회")
    class FindAll {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long workspaceId = 1L;
            Long userId = 3L;

            Category category = new Category();
            category.setId(1L);
            category.setName("백엔드");

            when(categoryMapper.findAll(workspaceId)).thenReturn(List.of(category));

            // when
            List<CategoryResponse> result = categoryService.findAll(workspaceId, userId);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.getFirst().id()).isEqualTo(1L);
            assertThat(result.getFirst().name()).isEqualTo("백엔드");
            verify(categoryMapper).findAll(workspaceId);
        }

        @Test
        @DisplayName("실패 - 멤버 아님")
        void notMember() {
            // given
            Long workspaceId = 1L;
            Long userId = 3L;

            doThrow(new AccessDeniedException("워크스페이스 멤버가 아닙니다."))
                    .when(permissionChecker).checkMember(workspaceId, userId);

            // when & then
            assertThatThrownBy(() -> categoryService.findAll(workspaceId, userId))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("워크스페이스 멤버가 아닙니다.");
        }
    }

    @Nested
    @DisplayName("카테고리 삭제")
    class Delete {
        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long workspaceId = 1L;
            Long categoryId = 10L;
            Long userId = 3L;

            // when
            categoryService.delete(workspaceId, categoryId, userId);

            // then
            verify(categoryMapper).delete(categoryId);
        }

        @Test
        @DisplayName("실패 - 관리자 아님")
        void notAdmin() {
            // given
            Long workspaceId = 1L;
            Long categoryId = 10L;
            Long userId = 3L;

            doThrow(new AccessDeniedException("관리자만 가능합니다."))
                    .when(permissionChecker).checkAdmin(workspaceId, userId);

            // when & then
            assertThatThrownBy(() -> categoryService.delete(workspaceId, categoryId, userId))
                    .isInstanceOf(AccessDeniedException.class)
                    .hasMessage("관리자만 가능합니다.");
        }
    }



}