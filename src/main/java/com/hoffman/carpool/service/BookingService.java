package com.hoffman.carpool.service;

import com.hoffman.carpool.domain.BookingReference;

import java.util.List;

public interface BookingService {
    BookingReference createRiderBooking(BookingReference bookingReference);
    BookingReference findBookingReference(Long id);
    List<BookingReference> findAll();
}