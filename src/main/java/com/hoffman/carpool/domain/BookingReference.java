package com.hoffman.carpool.domain;

import javax.persistence.*;
import java.util.Date;

@Entity
public class BookingReference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bookingReferenceId;
    private String bookingStatus;
    private Date date;
    private String passengerNumber;
    private String departureLocation;
    private String arrivalLocation;
    private String paymentMethod;
    private String notes;

    private String accountType;
    private String author;

    @ManyToOne
    @JoinColumn(name = "driver_account_id")
    private DriverAccount driverAccount;

    @ManyToOne
    @JoinColumn(name = "rider_account_id")
    private RiderAccount riderAccount;

    public long getBookingReferenceId() {
        return bookingReferenceId;
    }

    public void setBookingReferenceId(long bookingReferenceId) {
        this.bookingReferenceId = bookingReferenceId;
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

    public String getPassengerNumber() {
        return passengerNumber;
    }

    public void setPassengerNumber(String passengerNumber) {
        this.passengerNumber = passengerNumber;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public DriverAccount getDriverAccount() {
        return driverAccount;
    }

    public void setDriverAccount(DriverAccount driverAccount) {
        this.driverAccount = driverAccount;
    }

    public RiderAccount getRiderAccount() {
        return riderAccount;
    }

    public void setRiderAccount(RiderAccount riderAccount) {
        this.riderAccount = riderAccount;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
