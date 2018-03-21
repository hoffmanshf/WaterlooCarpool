package com.hoffman.carpool.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class DriverAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "driverAccountId")
    private Long driverAccountId;
    private String username;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @OneToOne
    private Car car;

    @OneToMany(mappedBy = "driverAccount", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<BookingReference> bookingReferences;

    public Long getDriverAccountId() {
        return driverAccountId;
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

    public Car getCar() {
        return car;
    }

    public void setCar(Car car) {
        this.car = car;
    }
}
