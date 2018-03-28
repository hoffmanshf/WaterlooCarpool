package com.hoffman.carpool.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Entity
public class BookingReference {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long bookingReferenceId;
    private String bookingStatus;

    private Date date;
    private GregorianCalendar arrivalTime;
    private String dateForSearch;
    private String dayOfWeek;
    private String dayOfMonth;
    private String month;
    private String time;

    private int passengerNumber;
    private String departure;
    private String arrival;
    private String paymentMethod;
    private int price;
    private String notes;
//    private String cancelNotes;

    private String duration;
    private String distance;

    private String accountType;
    private String author;

    private Boolean owner;

    @ManyToOne
    @JoinColumn(name = "driver_account_id")
    private DriverAccount driverAccount;
    
    @ManyToMany
    @JoinTable(name = "PASSENGER_LIST",
            joinColumns = { @JoinColumn(name = "booking_reference_id") },
            inverseJoinColumns = { @JoinColumn(name = "rider_account_id") })
    private List<RiderAccount> passengerList;

    @ManyToMany
    @JoinTable(name = "CANCELLED_PASSENGER_LIST",
            joinColumns = { @JoinColumn(name = "booking_reference_id") },
            inverseJoinColumns = { @JoinColumn(name = "rider_account_id") })
    private List<RiderAccount> cancelledPassengerList;

    @OneToMany(mappedBy = "bookingReference", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Notification> notificationList;

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

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
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

    public List<RiderAccount> getPassengerList() {
        return passengerList;
    }

    public void setPassengerList(List<RiderAccount> passengerList) {
        this.passengerList = passengerList;
    }

    public List<RiderAccount> getCancelledPassengerList() {
        return cancelledPassengerList;
    }

    public void setCancelledPassengerList(List<RiderAccount> cancelledPassengerList) {
        this.cancelledPassengerList = cancelledPassengerList;
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

    public String getDateForSearch() {
        return dateForSearch;
    }

    public void setDateForSearch(String dateForSearch) {
        this.dateForSearch = dateForSearch;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public GregorianCalendar getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(GregorianCalendar arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public List<Notification> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<Notification> notificationList) {
        this.notificationList = notificationList;
    }

    //    public String getCancelNotes() {
//        return cancelNotes;
//    }
//
//    public void setCancelNotes(String cancelNotes) {
//        this.cancelNotes = cancelNotes;
//    }
}
