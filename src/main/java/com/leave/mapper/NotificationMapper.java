package com.leave.mapper;

import com.leave.entity.Notification;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface NotificationMapper {
    int insert(Notification notification);
    List<Notification> findByUserId(@Param("userId") Long userId,
                                     @Param("offset") int offset,
                                     @Param("limit") int limit);
    int markRead(@Param("id") Long id);
    int countUnread(@Param("userId") Long userId);
}
