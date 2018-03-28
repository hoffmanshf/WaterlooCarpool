package com.hoffman.carpool.repository;

import com.hoffman.carpool.domain.entity.Notification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;

@Transactional
public interface NotificationRepository extends CrudRepository<Notification, Long> {
    @Modifying
    @Transactional
    @Query("delete from Notification u where u.notificationId = ?1")
    void deleteByNotificationId(Long notificationId);
}
