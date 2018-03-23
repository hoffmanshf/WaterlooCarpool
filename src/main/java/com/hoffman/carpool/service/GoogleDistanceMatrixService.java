package com.hoffman.carpool.service;

import com.hoffman.carpool.domain.BookingReference;

import java.util.GregorianCalendar;

public interface GoogleDistanceMatrixService {
    void estimateRouteTime(String departure, String arrival, GregorianCalendar calendar, BookingReference bookingReference);
}
