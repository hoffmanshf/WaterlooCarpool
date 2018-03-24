package com.hoffman.carpool.service;

import com.hoffman.carpool.domain.BookingReference;
import com.hoffman.carpool.domain.BookingReferenceReservation;
import com.hoffman.carpool.domain.User;
import org.springframework.data.domain.Sort;

import java.security.Principal;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public interface BookingService {
    void createBooking(BookingReference bookingReference, String source, String accountType, Principal principal);
    void acceptDriverBooking(User user, Integer seatsReserved, BookingReference bookingReference);
    BookingReference findBookingReference(Long id);
    List<BookingReference> findAll();
    BookingReference saveBooking(BookingReference bookingReference);
}
