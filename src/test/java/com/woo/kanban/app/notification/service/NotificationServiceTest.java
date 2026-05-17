package com.woo.kanban.app.notification.service;

import com.woo.kanban.app.notification.dto.NotificationResponse;
import com.woo.kanban.app.notification.mapper.NotificationMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @InjectMocks
    NotificationService notificationService;

    @Mock
    NotificationMapper notificationMapper;

    @Nested
    @DisplayName("알림 목록 조회")
    class FindAll {

        @Test
        @DisplayName("성공")
        void success() {
            // given
            Long userId = 1L;
            NotificationResponse notification = new NotificationResponse(
                    10L, "새 태스크 배정", "task1에 담당자로 배정되었습니다.", false, LocalDateTime.now()
            );
            when(notificationMapper.findByUserId(userId)).thenReturn(List.of(notification));

            // when
            List<NotificationResponse> result = notificationService.findAll(userId);

            // then
            assertThat(result).hasSize(1);
            assertThat(result.getFirst().title()).isEqualTo("새 태스크 배정");
            assertThat(result.getFirst().isRead()).isFalse();
            verify(notificationMapper).findByUserId(userId);
        }

        @Test
        @DisplayName("성공 - 알림 없음")
        void empty() {
            // given
            Long userId = 1L;
            when(notificationMapper.findByUserId(userId)).thenReturn(List.of());

            // when
            List<NotificationResponse> result = notificationService.findAll(userId);

            // then
            assertThat(result).isEmpty();
            verify(notificationMapper).findByUserId(userId);
        }
    }

    @Nested
    @DisplayName("읽음 처리")
    class MarkAsRead {

        @Test
        @DisplayName("성공")
        void success() {

            // given
            Long notificationId = 10L;
            Long userId = 1L;

            // when
            notificationService.markAsRead(notificationId, userId);

            // then
            verify(notificationMapper).updateIsRead(notificationId, userId);
        }
    }

    @Nested
    @DisplayName("전체 읽음 처리")
    class MarkAllAsRead {

        @Test
        @DisplayName("성공")
        void success() {

            // given
            Long userId = 1L;

            // when
            notificationService.markAllAsRead(userId);

            // then
            verify(notificationMapper).updateAllIsRead(userId);
        }
    }

}