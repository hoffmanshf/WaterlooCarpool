package com.hoffman.carpool.service;

import com.hoffman.carpool.domain.BookingReference;
import com.hoffman.carpool.domain.DriverAccount;
import com.hoffman.carpool.domain.RiderAccount;
import com.hoffman.carpool.domain.User;

import java.util.List;

public interface AccountService {
    DriverAccount createDriverAccount(User user);
    RiderAccount createRiderAccount(User user);
    List<BookingReference> getAccountBookingReference(String sort, String accountType, User user);
    List<BookingReference> getAccountSearchResult(String sort, String accountType, String date, String arrival, String departure, User user, String... passengerNumber);
}
