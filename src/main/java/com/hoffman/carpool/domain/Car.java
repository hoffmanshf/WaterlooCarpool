package com.hoffman.carpool.domain;

public class Car {

    private String carId;
    private String carManufacturer;
    private String carModel;
    private String carColour;
    private String carYear;
    private String carLicensePlateNumber;

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCarManufacturer() {
        return carManufacturer;
    }

    public void setCarManufacturer(String carManufacturer) {
        this.carManufacturer = carManufacturer;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarColour() {
        return carColour;
    }

    public void setCarColour(String carColour) {
        this.carColour = carColour;
    }

    public String getCarYear() {
        return carYear;
    }

    public void setCarYear(String carYear) {
        this.carYear = carYear;
    }

    public String getCarLicensePlateNumber() {
        return carLicensePlateNumber;
    }

    public void setCarLicensePlateNumber(String carLicensePlateNumber) {
        this.carLicensePlateNumber = carLicensePlateNumber;
    }
}
