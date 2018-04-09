package com.hoffman.carpool.service;

import com.hoffman.carpool.domain.entity.BookingReference;
import com.hoffman.carpool.domain.entity.User;

import java.security.Principal;
import java.util.List;

public interface BookingService {
    void createBooking(BookingReference bookingReference, String source, String accountType, Principal principal);
    void acceptDriverBooking(User user, Integer seatsReserved, BookingReference bookingReference);
    void acceptRiderBooking(User user, BookingReference bookingReference);
    BookingReference findBookingReference(Long id);
    List<BookingReference> findAll();
    List<BookingReference> findByAccountType(String accountType);
    BookingReference saveBooking(BookingReference bookingReference);
}
