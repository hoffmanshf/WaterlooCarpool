package com.hoffman.carpool.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class DriverBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long driverBookingId;
    private String bookingStatus;
    private Date date;
    private String departureLocation;
    private String arrivalLocation;
    private String description;

    public DriverBooking() {
    }

    public DriverBooking(String bookingStatus, Date date, String departureLocation, String arrivalLocation, String description, DriverAccount driverAccount) {
        this.bookingStatus = bookingStatus;
        this.date = date;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.description = description;
        this.driverAccount = driverAccount;
    }

    @ManyToOne
    @JoinColumn(name = "driver_account_id")
    private DriverAccount driverAccount;

    public long getDriverBookingId() {
        return driverBookingId;
    }

    public void setDriverBookingId(long driverBookingId) {
        this.driverBookingId = driverBookingId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public DriverAccount getDriverAccount() {
        return driverAccount;
    }

    public void setDriverAccount(DriverAccount driverAccount) {
        this.driverAccount = driverAccount;
    }
}
