package com.hoffman.carpool.domain;

import java.util.List;

public class DriverAccount {

    private Long driverAccountId;
    private int accountNumber;
    private Car car;
    private List<Booking> bookingList;

    public Long getDriverAccountId() {
        return driverAccountId;
    }

    public void setDriverAccountId(Long driverAccountId) {
        this.driverAccountId = driverAccountId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }
}
