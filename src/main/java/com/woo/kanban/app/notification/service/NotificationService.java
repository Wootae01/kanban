package com.woo.kanban.app.notification.service;

import com.woo.kanban.app.notification.dto.NotificationResponse;
import com.woo.kanban.app.notification.mapper.NotificationMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationService {

    private final NotificationMapper notificationMapper;
    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();


    public void send(Long userId, String title, String message) {
        notificationMapper.insert(userId, title, message);
        SseEmitter emitter = emitters.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(Map.of("title", title, "message", message)));
            } catch (IOException e) {
                log.error("SSE 전송 실패 userId={}", userId, e);
                emitters.remove(userId);
            }

        }
    }

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = new SseEmitter(30 * 60 * 1000L);
        emitters.put(userId, emitter);
        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError(e -> {
            log.error("SSE error userId={}", userId, e);
            emitters.remove(userId);
        });

        try {
            emitter.send(SseEmitter.event().name("connect").data("connected"));
        } catch (IOException e) {
            log.error("SSE initial error userId={}", userId, e);
            emitters.remove(userId);
        }

        return emitter;
    }

    public List<NotificationResponse> findAll(Long userId) {
        return notificationMapper.findByUserId(userId);
    }

    // 알림 읽음 처리
    public void markAsRead(Long notificationId, Long userId) {
        notificationMapper.updateIsRead(notificationId, userId);
    }

    // 모든 알림 읽음 처리
    public void markAllAsRead(Long userId) {
        notificationMapper.updateAllIsRead(userId);
    }
}
