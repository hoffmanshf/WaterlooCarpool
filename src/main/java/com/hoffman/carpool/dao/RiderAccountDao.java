package com.hoffman.carpool.dao;

import com.hoffman.carpool.domain.RiderAccount;
import org.springframework.data.repository.CrudRepository;

public interface RiderAccountDao extends CrudRepository<RiderAccount,Long> {
    RiderAccount findByRiderAccountId(Long riderAccountId);
}
