package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.dao.CarDao;
import com.hoffman.carpool.domain.Car;
import com.hoffman.carpool.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CarServiceImpl implements CarService{

    @Autowired
    private CarDao carDao;
    @Override
    public Car createCar(Car car) {
        return carDao.save(car);
    }
}
