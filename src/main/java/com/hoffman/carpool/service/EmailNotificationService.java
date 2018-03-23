package com.hoffman.carpool.service;

import com.hoffman.carpool.domain.CalendarEvent;

public interface EmailNotificationService {

    void sendNotification (String toAddress, final CalendarEvent calendarEvent);
}
