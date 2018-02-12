package com.hoffman.carpool.domain;

import java.util.List;

public class RiderAccount {

    private Long riderAccountId;
    private int accountNumber;
    private List<Booking> bookingList;

    public Long getRiderAccountId() {
        return riderAccountId;
    }

    public void setRiderAccountId(Long riderAccountId) {
        this.riderAccountId = riderAccountId;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(int accountNumber) {
        this.accountNumber = accountNumber;
    }

    public List<Booking> getBookingList() {
        return bookingList;
    }

    public void setBookingList(List<Booking> bookingList) {
        this.bookingList = bookingList;
    }
}
