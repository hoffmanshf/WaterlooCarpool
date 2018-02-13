package com.hoffman.carpool.dao;

import com.hoffman.carpool.domain.DriverAccount;
import org.springframework.data.repository.CrudRepository;

public interface DriverAccountDao extends CrudRepository<DriverAccount,Long> {
    DriverAccount findByDriverAccountId(Long driverAccountId);
}
