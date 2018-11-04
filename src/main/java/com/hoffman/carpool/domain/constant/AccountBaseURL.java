package com.hoffman.carpool.domain.constant;

public class AccountBaseURL {
    public static final String driverAccountBaseURL = "https://waterloocarpool-demo.herokuapp.com/account/driver-offer";
    public static final String driverAccountSearchBaseURL = "https://waterloocarpool-demo.herokuapp.com/account/driver-offer/search";
    public static final String driverAccountSearchPassengerBaseURL = "https://waterloocarpool-demo.herokuapp.com/account/driver-offer/search-passenger";

    public static final String riderAccountBaseURL = "https://waterloocarpool-demo.herokuapp.com/account/rider-request";
    public static final String riderAccountSearchBaseURL = "https://waterloocarpool-demo.herokuapp.com/account/rider-request/search";
    public static final String riderAccountSearchPassengerBaseURL = "https://waterloocarpool-demo.herokuapp.com/account/rider-request/search-passenger";
  
    public AccountBaseURL() {
        // empty to avoid instantiating this constant class
    }
}
