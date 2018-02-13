package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.dao.DriverAccountDao;
import com.hoffman.carpool.dao.RiderAccountDao;
import com.hoffman.carpool.domain.DriverAccount;
import com.hoffman.carpool.domain.RiderAccount;
import com.hoffman.carpool.domain.User;
import com.hoffman.carpool.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private DriverAccountDao driverAccountDao;

    @Autowired
    private RiderAccountDao riderAccountDao;

    @Override
    public DriverAccount createDriverAccount(User user) {
        DriverAccount driverAccount = new DriverAccount();
        driverAccount.setEmail(user.getEmail());
        driverAccount.setPhone(user.getPhone());
        driverAccount.setUsername(user.getUsername());
        driverAccountDao.save(driverAccount);
        return driverAccountDao.findByDriverAccountId(driverAccount.getDriverAccountId());
    }

    @Override
    public RiderAccount createRiderAccount(User user) {
        RiderAccount riderAccount = new RiderAccount();
        riderAccount.setEmail(user.getEmail());
        riderAccount.setPhone(user.getPhone());
        riderAccount.setUsername(user.getUsername());
        riderAccountDao.save(riderAccount);
        return riderAccountDao.findByRiderAccountId(riderAccount.getRiderAccountId());
    }
}
