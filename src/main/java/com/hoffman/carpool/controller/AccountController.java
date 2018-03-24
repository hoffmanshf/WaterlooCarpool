package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.*;
import com.hoffman.carpool.service.BookingService;
import com.hoffman.carpool.service.UserService;
import com.hoffman.carpool.util.BookingReferenceUtil;
import com.hoffman.carpool.domain.PageWrapper;
import com.hoffman.carpool.util.PaginationUtil;
import com.hoffman.carpool.util.SortingUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class AccountController {

    private static final String riderAccountType = "Passenger";
    private static final String driverAccountType = "Driver";

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private BookingReferenceUtil bookingReferenceUtil;

    @Autowired
    private SortingUtil sortingUtil;

    @Autowired
    private PaginationUtil paginationUtil;

    @RequestMapping(value = "/riderAccount", method = RequestMethod.GET)
    public String riderAccount(Model model, Principal principal,
                               @RequestParam(value = "page") Optional<Integer> page) {
        User user = userService.findByUsername(principal.getName());
        RiderAccount riderAccount = user.getRiderAccount();

        List<BookingReference> bookingReferences = bookingService.findAll(SortingType.sortByDateASC);

        bookingReferenceUtil.BookingReferenceStatusProcessor(riderAccountType, bookingReferences);
        bookingReferences = bookingReferenceUtil.BookingReferenceProcessor(riderAccountType, user, bookingReferences);
        model.addAttribute("riderAccount", riderAccount);
        model.addAttribute("bookingReferences", bookingReferences);

        return "riderAccount";
    }

    @RequestMapping(value = "/driverAccount", method = RequestMethod.GET)
    public String driverAccount(Model model, Principal principal,
                                @RequestParam(value = "sort", required = false) String sort,
                                @RequestParam(value = "page") Optional<Integer> page) {

        User user = userService.findByUsername(principal.getName());
        DriverAccount driverAccount = user.getDriverAccount();

        if (sort == null) {
            final String URL = "http://localhost:8080/account/driverAccount";
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL);
            final URI uri = builder.build().encode().toUri();
            model.addAttribute("uri", uri);
            sort = "date-asc";
        } else {
            final String URL = "http://localhost:8080/account/driverAccount";
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam("sort", sort);
            final URI uri = builder.build().encode().toUri();
            model.addAttribute("uri", uri);
        }

        Sort sortType = sortingUtil.getSortType(sort);

        List<BookingReference> bookingReferences = bookingService.findAll(sortType);
        bookingReferenceUtil.BookingReferenceStatusProcessor(driverAccountType, bookingReferences);
        bookingReferences = bookingReferenceUtil.BookingReferenceProcessor(driverAccountType, user, bookingReferences);

        if (bookingReferences.size() == 0) {
            return "driverAccountNoResult";
        }

        PageWrapper wrapper = paginationUtil.PaginationProcessor(model, page, bookingReferences);

        final String URL2 = "http://localhost:8080/account/driverAccount";
        final UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(URL2);
        final URI uri2 = builder2.build().encode().toUri();
        model.addAttribute("uri2", uri2);

        model.addAttribute("driverAccount", driverAccount);
        model.addAttribute("wrapper", wrapper);
        return "driverAccount";
    }

    @RequestMapping(value = "/driverAccount/search", method = RequestMethod.GET)
    public String searchDriverBooking(@RequestParam(value = "arrival") String arrival,
                                      @RequestParam(value = "departure") String departure,
                                      @RequestParam(value = "date") String date,
                                      @RequestParam(value = "sort", required = false) String sort,
                                      @RequestParam(value = "page") Optional<Integer> page,
                                      Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        DriverAccount driverAccount = user.getDriverAccount();
        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();

        final String arrivalCity = bookingReferenceUtil.AddressCityProcessor(arrival);
        final String departureCity = bookingReferenceUtil.AddressCityProcessor(departure);

        if (sort == null) {
            final String URL = "http://localhost:8080/account/driverAccount/search";
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam("departure", departure)
                    .queryParam("arrival", arrival)
                    .queryParam("date", date);
            final URI uri = builder.build().encode().toUri();
            model.addAttribute("uri", uri);
            sort = "date-asc";
        } else {
            final String URL = "http://localhost:8080/account/driverAccount/search";
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam("departure", departure)
                    .queryParam("arrival", arrival)
                    .queryParam("date", date)
                    .queryParam("sort", sort);
            final URI uri = builder.build().encode().toUri();
            model.addAttribute("uri", uri);
        }

        Sort sortType = sortingUtil.getSortType(sort);

        // arrival and departure are encoded in frontend and automatically decoded in backend
        if (date != null && StringUtils.isNotEmpty(date)) {
            bookingReferences = bookingService.searchBookingReference(arrivalCity, departureCity, date, sortType);
        } else {
            bookingReferences = bookingService.searchBookingReferenceWithoutDate(arrivalCity, departureCity, sortType);
        }

        bookingReferenceUtil.BookingReferenceStatusProcessor(driverAccountType, bookingReferences);
        bookingReferences = bookingReferenceUtil.BookingReferenceProcessor(driverAccountType, user, bookingReferences);
        model.addAttribute("bookingReferences", bookingReferences);

        PageWrapper wrapper = paginationUtil.PaginationProcessor(model, page, bookingReferences);

        final String URL2 = "http://localhost:8080/account/driverAccount/search";
        final UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(URL2)
                .queryParam("departure", departure)
                .queryParam("arrival", arrival)
                .queryParam("date", date);
        final URI uri2 = builder2.build().encode().toUri();
        model.addAttribute("uri2", uri2);

        model.addAttribute("wrapper", wrapper);
        model.addAttribute("driverAccount", driverAccount);
        if (bookingReferences.size() == 0) {
            return "driverAccountNoResult";
        } else {
            return "driverAccount";
        }

    }

    @RequestMapping(value = "/driverAccount/searchPassenger", method = RequestMethod.GET)
    public String searchPassengerDriverBooking(@RequestParam(value = "arrival") String arrival,
                                               @RequestParam(value = "departure") String departure,
                                               @RequestParam(value = "date") String date,
                                               @RequestParam(value = "passengerNumber") String passengerNumber,
                                               @RequestParam(value = "sort", required = false) String sort,
                                               @RequestParam(value = "page") Optional<Integer> page,
                                               Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        DriverAccount driverAccount = user.getDriverAccount();
        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();

        final String arrivalCity = bookingReferenceUtil.AddressCityProcessor(arrival);
        final String departureCity = bookingReferenceUtil.AddressCityProcessor(departure);

        if (sort == null) {
            final String URL = "http://localhost:8080/account/driverAccount/searchPassenger";
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam("departure", departure)
                    .queryParam("arrival", arrival)
                    .queryParam("date", date)
                    .queryParam("passengerNumber", passengerNumber);
            final URI uri = builder.build().encode().toUri();
            model.addAttribute("uri", uri);
            sort = "date-asc";
        } else {
            final String URL = "http://localhost:8080/account/driverAccount/searchPassenger";
            final UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(URL)
                    .queryParam("departure", departure)
                    .queryParam("arrival", arrival)
                    .queryParam("date", date)
                    .queryParam("passengerNumber", passengerNumber)
                    .queryParam("sort", sort);
            final URI uri = builder.build().encode().toUri();
            model.addAttribute("uri", uri);
        }

        Sort sortType = sortingUtil.getSortType(sort);

        // arrival and departure are encoded in frontend and automatically decoded in backend
        if (date != null && StringUtils.isNotEmpty(date)) {
            bookingReferences = bookingService.searchBookingReference(arrivalCity, departureCity, date, sortType);
        } else {
            bookingReferences = bookingService.searchBookingReferenceWithoutDate(arrivalCity, departureCity, sortType);
        }

        bookingReferenceUtil.BookingReferenceStatusProcessor(driverAccountType, bookingReferences);
        bookingReferences = bookingReferenceUtil.BookingReferenceProcessor(driverAccountType, user, bookingReferences);

        if (passengerNumber != null && StringUtils.isNotEmpty(passengerNumber)) {
            final int seats = Integer.valueOf(passengerNumber);
            List<BookingReference> filteredBookingReferences = new ArrayList<BookingReference>();
            for (final BookingReference reference: bookingReferences) {
                if (seats <= reference.getPassengerNumber()) {
                    filteredBookingReferences.add(reference);
                }
            }
            bookingReferences = filteredBookingReferences;
        }

        PageWrapper wrapper = paginationUtil.PaginationProcessor(model, page, bookingReferences);

        final String URL2 = "http://localhost:8080/account/driverAccount/searchPassenger";
        final UriComponentsBuilder builder2 = UriComponentsBuilder.fromHttpUrl(URL2)
                .queryParam("departure", departure)
                .queryParam("arrival", arrival)
                .queryParam("date", date)
                .queryParam("passengerNumber", passengerNumber);
        final URI uri2 = builder2.build().encode().toUri();

        model.addAttribute("wrapper", wrapper);
        model.addAttribute("driverAccount", driverAccount);
        model.addAttribute("uri2", uri2);
        if (bookingReferences.size() == 0) {
            return "driverAccountNoResult";
        } else {
            return "driverAccount";
        }
    }

}
