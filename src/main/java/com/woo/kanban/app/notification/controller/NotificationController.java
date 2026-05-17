package com.woo.kanban.app.notification.controller;

import com.woo.kanban.app.notification.dto.NotificationResponse;
import com.woo.kanban.app.notification.service.NotificationService;
import com.woo.kanban.app.user.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    // 알림 목록
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotificationList(
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        List<NotificationResponse> result = notificationService.findAll(userDetails.getId());
        return ResponseEntity.ok(result);
    }

    // 읽음 처리
    @PatchMapping("/{id}/read")
    public ResponseEntity<Void> readNotification(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {

        notificationService.markAsRead(id, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/read-all")
    public ResponseEntity<Void> readAll(@AuthenticationPrincipal CustomUserDetails userDetails) {

        notificationService.markAllAsRead(userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/stream", produces = "text/event-stream")
    public SseEmitter stream(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return notificationService.subscribe(userDetails.getId());
    }

}
