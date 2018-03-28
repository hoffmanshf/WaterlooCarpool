package com.hoffman.carpool.service;

import com.hoffman.carpool.domain.entity.Notification;

public interface NotificationService {
    Notification saveNotification(Notification notification);
    void deleteNotification(Long id);
}
