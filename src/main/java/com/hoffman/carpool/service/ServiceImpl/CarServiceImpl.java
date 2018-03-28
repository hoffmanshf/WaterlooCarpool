package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.repository.CarRepository;
import com.hoffman.carpool.domain.entity.Car;
import com.hoffman.carpool.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl implements CarService{

    @Autowired
    private CarRepository carRepository;

    @Override
    public Car createCar(Car car) {
        return carRepository.save(car);
    }
}
