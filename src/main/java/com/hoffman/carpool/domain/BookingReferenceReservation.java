package com.hoffman.carpool.domain;

import com.hoffman.carpool.domain.entity.RiderAccount;

public class BookingReferenceReservation {

    private RiderAccount passenger;
    private int seatsOccupied;

    public RiderAccount getPassenger() {
        return passenger;
    }

    public void setPassenger(RiderAccount passenger) {
        this.passenger = passenger;
    }

    public int getSeatsOccupied() {
        return seatsOccupied;
    }

    public void setSeatsOccupied(int seatsOccupied) {
        this.seatsOccupied = seatsOccupied;
    }
}
