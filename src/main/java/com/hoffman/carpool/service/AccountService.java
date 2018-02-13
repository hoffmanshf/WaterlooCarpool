package com.hoffman.carpool.service;

import com.hoffman.carpool.domain.DriverAccount;
import com.hoffman.carpool.domain.RiderAccount;
import com.hoffman.carpool.domain.User;

public interface AccountService {
    DriverAccount createDriverAccount(User user);
    RiderAccount createRiderAccount(User user);
}
