package com.hoffman.carpool.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class DriverAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "driverAccountId", unique = true)
    private Long driverAccountId;
    private int accountNumber;

    @OneToOne
    private Car car;

    @OneToMany(mappedBy = "driverAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<DriverBooking> driverBookingList;

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

    public List<DriverBooking> getDriverBookingList() {
        return driverBookingList;
    }

    public void setDriverBookingList(List<DriverBooking> driverBookingList) {
        this.driverBookingList = driverBookingList;
    }

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
