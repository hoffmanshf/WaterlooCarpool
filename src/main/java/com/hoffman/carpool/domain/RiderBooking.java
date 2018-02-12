package com.hoffman.carpool.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class RiderBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long riderBookingId;
    private String bookingStatus;
    private Date date;
    private String departureLocation;
    private String arrivalLocation;
    private String description;

    @ManyToOne
    @JoinColumn(name = "rider_account_id")
    private RiderAccount riderAccount;

    public RiderBooking() {
    }

    public RiderBooking(String bookingStatus, Date date, String departureLocation, String arrivalLocation, String description, RiderAccount riderAccount) {
        this.bookingStatus = bookingStatus;
        this.date = date;
        this.departureLocation = departureLocation;
        this.arrivalLocation = arrivalLocation;
        this.description = description;
        this.riderAccount = riderAccount;
    }

    public Long getRiderBookingId() {
        return riderBookingId;
    }

    public void setRiderBookingId(Long riderBookingId) {
        this.riderBookingId = riderBookingId;
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

    public RiderAccount getRiderAccount() {
        return riderAccount;
    }

    public void setRiderAccount(RiderAccount riderAccount) {
        this.riderAccount = riderAccount;
    }
}
