package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.repository.DriverAccountRepository;
import com.hoffman.carpool.repository.RiderAccountRepository;
import com.hoffman.carpool.domain.Car;
import com.hoffman.carpool.domain.DriverAccount;
import com.hoffman.carpool.domain.RiderAccount;
import com.hoffman.carpool.domain.User;
import com.hoffman.carpool.service.AccountService;
import com.hoffman.carpool.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private DriverAccountRepository driverAccountRepository;

    @Autowired
    private RiderAccountRepository riderAccountRepository;

    @Autowired
    private CarService carService;

    @Override
    public DriverAccount createDriverAccount(User user) {
        DriverAccount driverAccount = new DriverAccount();
        Car car = new Car();
        driverAccount.setEmail(user.getEmail());
        driverAccount.setPhone(user.getPhone());
        driverAccount.setUsername(user.getUsername());
        driverAccount.setFirstName(user.getFirstName());
        driverAccount.setLastName(user.getLastName());
        driverAccount.setCar(carService.createCar(car));
        driverAccountRepository.save(driverAccount);
        return driverAccountRepository.findByDriverAccountId(driverAccount.getDriverAccountId());
    }

    @Override
    public RiderAccount createRiderAccount(User user) {
        RiderAccount riderAccount = new RiderAccount();
        riderAccount.setEmail(user.getEmail());
        riderAccount.setPhone(user.getPhone());
        riderAccount.setUsername(user.getUsername());
        riderAccount.setFirstName(user.getFirstName());
        riderAccount.setLastName(user.getLastName());
        riderAccountRepository.save(riderAccount);
        return riderAccountRepository.findByRiderAccountId(riderAccount.getRiderAccountId());
    }
}
