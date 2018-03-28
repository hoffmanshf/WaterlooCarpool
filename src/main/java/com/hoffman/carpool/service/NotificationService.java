package com.hoffman.carpool.service;

import com.hoffman.carpool.domain.entity.BookingReference;
import com.hoffman.carpool.domain.entity.User;

public interface NotificationService {
    void sendAcceptedNotification(BookingReference bookingReference, User user);
    void sendCancelledNotification(BookingReference bookingReference, User user);
    void deleteNotification(Long id);
}
