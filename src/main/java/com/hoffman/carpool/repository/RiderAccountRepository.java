package com.hoffman.carpool.repository;

import com.hoffman.carpool.domain.RiderAccount;
import org.springframework.data.repository.CrudRepository;

public interface RiderAccountRepository extends CrudRepository<RiderAccount,Long> {
    RiderAccount findByRiderAccountId(Long riderAccountId);
}
