package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.domain.entity.BookingReference;
import com.hoffman.carpool.domain.entity.Notification;
import com.hoffman.carpool.domain.entity.User;
import com.hoffman.carpool.repository.NotificationRepository;
import com.hoffman.carpool.service.NotificationService;
import com.hoffman.carpool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationRepository notificationRepository;

    @Override
    public void sendAcceptedNotification(BookingReference bookingReference, User user) {

        User author = userService.findByUsername(bookingReference.getAuthor());
        Notification notification = new Notification();
        notification.setUser(author);
        notification.setAuthor(user.getUsername());
        notification.setBookingReference(bookingReference);
        notification.setContent(String.format("%s has accepted your booking", user.getUsername()));
        notificationRepository.save(notification);
    }

    @Override
    public void sendCancelledNotification(BookingReference bookingReference, User user) {

        User author = userService.findByUsername(bookingReference.getAuthor());
        Notification notification = new Notification();
        notification.setUser(author);
        notification.setAuthor(user.getUsername());
        notification.setBookingReference(bookingReference);
        notification.setContent(String.format("%s has cancelled your booking", user.getUsername()));
        notificationRepository.save(notification);
    }

    @Override
    public void deleteNotification(Long id) {
        notificationRepository.deleteByNotificationId(id);
    }
}
