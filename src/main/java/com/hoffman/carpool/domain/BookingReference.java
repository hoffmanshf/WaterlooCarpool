package com.hoffman.carpool.domain;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class BookingReference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bookingReferenceId;
    private String bookingStatus;

    private Date date;
    private String dayOfWeek;
    private String dayOfMonth;
    private String month;
    private String time;

    private int passengerNumber;
    private String departureLocation;
    private String arrivalLocation;
    private String paymentMethod;
    private String price;
    private String notes;

    private String accountType;
    private String author;

    private Boolean owner;

    @ManyToOne
    @JoinColumn(name = "driver_account_id")
    private DriverAccount driverAccount;

    @ManyToOne
    @JoinColumn(name = "rider_account_id")
    private RiderAccount riderAccount;
    
    @ManyToMany
    @JoinTable(name = "PASSENGER_LIST",
            joinColumns = { @JoinColumn(name = "booking_reference_id") },
            inverseJoinColumns = { @JoinColumn(name = "rider_account_id") })
    private List<RiderAccount> passengerList;

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

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(String dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(String dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
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

    public List<RiderAccount> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<RiderAccount> passengerList) {
        this.passengerList = passengerList;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Boolean getOwner() {
        return owner;
    }

    public void setOwner(Boolean owner) {
        this.owner = owner;
    }

    public int getPassengerNumber() {
        return passengerNumber;
    }

    public void setPassengerNumber(int passengerNumber) {
        this.passengerNumber = passengerNumber;
    }
}
