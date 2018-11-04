package com.hoffman.carpool.domain.constant;

public class AccountBaseURL {
    public static final String driverAccountBaseURL = "http://localhost:8080/account/driver-offer";
    public static final String driverAccountSearchBaseURL = "http://localhost:8080/account/driver-offer/search";
    public static final String driverAccountSearchPassengerBaseURL = "http://localhost:8080/account/driver-offer/search-passenger";

    public static final String riderAccountBaseURL = "http://localhost:8080/account/rider-request";
    public static final String riderAccountSearchBaseURL = "http://localhost:8080/account/rider-request/search";
    public static final String riderAccountSearchPassengerBaseURL = "http://localhost:8080/account/rider-request/search-passenger";

    public AccountBaseURL() {
        // empty to avoid instantiating this constant class
    }
}
