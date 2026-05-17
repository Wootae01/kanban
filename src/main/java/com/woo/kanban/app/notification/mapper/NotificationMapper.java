package com.woo.kanban.app.notification.mapper;

import com.woo.kanban.app.notification.dto.NotificationResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper {
    List<NotificationResponse> findByUserId(Long userId);

    void updateIsRead(@Param("notificationId") Long notificationId, @Param("userId") Long userId);

    void updateAllIsRead(@Param("userId") Long userId);

    void insert(@Param("userId") Long userId, @Param("title") String title, @Param("message") String message);
}
