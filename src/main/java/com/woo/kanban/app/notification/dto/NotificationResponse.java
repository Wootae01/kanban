package com.woo.kanban.app.notification.dto;

import java.time.LocalDateTime;

public record NotificationResponse(
        Long id,
        String title,
        String message,
        boolean isRead,
        LocalDateTime createdAt
) {
}