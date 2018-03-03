package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.dao.CarDao;
import com.hoffman.carpool.dao.DriverAccountDao;
import com.hoffman.carpool.dao.RiderAccountDao;
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
    private DriverAccountDao driverAccountDao;

    @Autowired
    private RiderAccountDao riderAccountDao;

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
        driverAccountDao.save(driverAccount);
        return driverAccountDao.findByDriverAccountId(driverAccount.getDriverAccountId());
    }

    @Override
    public RiderAccount createRiderAccount(User user) {
        RiderAccount riderAccount = new RiderAccount();
        riderAccount.setEmail(user.getEmail());
        riderAccount.setPhone(user.getPhone());
        riderAccount.setUsername(user.getUsername());
        riderAccount.setFirstName(user.getFirstName());
        riderAccount.setLastName(user.getLastName());
        riderAccountDao.save(riderAccount);
        return riderAccountDao.findByRiderAccountId(riderAccount.getRiderAccountId());
    }
}
