package com.hoffman.carpool.repository;

import com.hoffman.carpool.domain.entity.Car;
import org.springframework.data.repository.CrudRepository;

public interface CarRepository extends CrudRepository<Car,Long> {

}
