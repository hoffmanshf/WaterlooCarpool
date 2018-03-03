package com.hoffman.carpool.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class RiderAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "riderAccountId")
    private Long riderAccountId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @OneToMany(mappedBy = "riderAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookingReference> bookingReferences;

    public Long getRiderAccountId() {
        return riderAccountId;
    }

    public void setRiderAccountId(Long riderAccountId) {
        this.riderAccountId = riderAccountId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<BookingReference> getBookingReferences() {
        return bookingReferences;
    }

    public void setBookingReferences(List<BookingReference> bookingReferences) {
        this.bookingReferences = bookingReferences;
    }
}
