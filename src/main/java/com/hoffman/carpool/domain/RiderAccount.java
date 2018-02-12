package com.hoffman.carpool.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class RiderAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "riderAccountId", unique = true)
    private Long riderAccountId;
    private int accountNumber;

    @OneToMany(mappedBy = "riderAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RiderBooking> riderBookingList;

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

    public List<RiderBooking> getRiderBookingList() {
        return riderBookingList;
    }

    public void setRiderBookingList(List<RiderBooking> riderBookingList) {
        this.riderBookingList = riderBookingList;
    }
}
