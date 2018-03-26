package com.hoffman.carpool.repository;

import com.hoffman.carpool.domain.DriverAccount;
import org.springframework.data.repository.CrudRepository;

public interface DriverAccountRepository extends CrudRepository<DriverAccount,Long> {
    DriverAccount findByDriverAccountId(Long driverAccountId);
}
