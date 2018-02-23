package com.hoffman.carpool.dao;

import com.hoffman.carpool.domain.Car;
import org.springframework.data.repository.CrudRepository;

public interface CarDao extends CrudRepository<Car,Long> {

}
