package com.hoffman.carpool.util;

import com.hoffman.carpool.domain.entity.BookingReference;
import com.hoffman.carpool.domain.BookingReferenceReservation;
import com.hoffman.carpool.domain.entity.RiderAccount;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ReservationUtil {
    public static List<BookingReferenceReservation> getBookingReservationList(final BookingReference bookingReference) {

        final List<RiderAccount> passengers = bookingReference.getPassengerList();
        final List<RiderAccount> distinctPassengers = passengers.stream()
                .distinct()
                .collect(Collectors.toList());
        List<BookingReferenceReservation> reservations = new ArrayList<>();
        for (RiderAccount distinctPassenger: distinctPassengers) {
            int seatsOccupied = 0;
            BookingReferenceReservation reservation = new BookingReferenceReservation();
            for (RiderAccount passenger: passengers) {
                if (distinctPassenger.getUsername().equalsIgnoreCase(passenger.getUsername())) {
                    seatsOccupied += 1;
                }
            }
            reservation.setPassenger(distinctPassenger);
            reservation.setSeatsOccupied(seatsOccupied);
            reservations.add(reservation);
        }
        return reservations;
    }
}
