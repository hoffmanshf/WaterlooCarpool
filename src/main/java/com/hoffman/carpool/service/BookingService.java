package com.hoffman.carpool.service;

import com.hoffman.carpool.domain.BookingReference;

import java.util.Date;
import java.util.List;

public interface BookingService {
    BookingReference createBooking(BookingReference bookingReference);
    BookingReference findBookingReference(Long id);
    List<BookingReference> findAll();
    BookingReference saveBooking(BookingReference bookingReference);
    List<BookingReference> searchBookingReference(String arrival, String departure, String date);
    List<BookingReference> searchBookingReferenceWithoutDate(String arrival, String departure);
}
