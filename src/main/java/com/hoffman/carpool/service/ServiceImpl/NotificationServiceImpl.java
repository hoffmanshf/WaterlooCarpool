package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.domain.entity.Notification;
import com.hoffman.carpool.repository.NotificationRepository;
import com.hoffman.carpool.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public Notification saveNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteByNotificationId(id);
    }
}
