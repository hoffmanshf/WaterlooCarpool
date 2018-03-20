package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.*;
import com.hoffman.carpool.service.BookingService;
import com.hoffman.carpool.service.UserService;
import com.hoffman.carpool.util.PageWrapper;
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
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class AccountController {

    private static final String riderAccountType = "Passenger";
    private static final String driverAccountType = "Driver";

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int PAGE_SIZE = 5;
    private static final int INITIAL_PAGE = 0;

    private static final Sort sortByDateASC = new Sort(Sort.Direction.ASC, "date");
    private static final Sort sortByPriceASC = new Sort(Sort.Direction.ASC, "price");
    private static final Sort sortByDateDESC = new Sort(Sort.Direction.DESC, "date");
    private static final Sort sortByPriceDESC = new Sort(Sort.Direction.DESC, "price");

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @RequestMapping(value = "/riderAccount", method = RequestMethod.GET)
    public String riderAccount(Model model, Principal principal,
                               @RequestParam(value = "page") Optional<Integer> page) {
        User user = userService.findByUsername(principal.getName());
        RiderAccount riderAccount = user.getRiderAccount();

        List<BookingReference> bookingReferences = bookingService.findAll(sortByDateASC);

        BookingReferenceStatusProcessor(riderAccountType, bookingReferences);
        bookingReferences = BookingReferenceProcessor(riderAccountType, user, bookingReferences);
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

        Sort sortType = getSortType(sort);

        List<BookingReference> bookingReferences = bookingService.findAll(sortType);
        BookingReferenceStatusProcessor(driverAccountType, bookingReferences);
        bookingReferences = BookingReferenceProcessor(driverAccountType, user, bookingReferences);

        if (bookingReferences.size() == 0) {
            return "driverAccountNoResult";
        }

        PageWrapper wrapper = PaginationProcessor(model, page, bookingReferences);

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

        final String arrivalCity = AddressCityProcessor(arrival);
        final String departureCity = AddressCityProcessor(departure);

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

        Sort sortType = getSortType(sort);

        // arrival and departure are encoded in frontend and automatically decoded in backend
        if (date != null && StringUtils.isNotEmpty(date)) {
            bookingReferences = bookingService.searchBookingReference(arrivalCity, departureCity, date, sortType);
        } else {
            bookingReferences = bookingService.searchBookingReferenceWithoutDate(arrivalCity, departureCity, sortType);
        }

        BookingReferenceStatusProcessor(driverAccountType, bookingReferences);
        bookingReferences = BookingReferenceProcessor(driverAccountType, user, bookingReferences);
        model.addAttribute("bookingReferences", bookingReferences);

        PageWrapper wrapper = PaginationProcessor(model, page, bookingReferences);

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

        final String arrivalCity = AddressCityProcessor(arrival);
        final String departureCity = AddressCityProcessor(departure);

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

        Sort sortType = getSortType(sort);

        // arrival and departure are encoded in frontend and automatically decoded in backend
        if (date != null && StringUtils.isNotEmpty(date)) {
            bookingReferences = bookingService.searchBookingReference(arrivalCity, departureCity, date, sortType);
        } else {
            bookingReferences = bookingService.searchBookingReferenceWithoutDate(arrivalCity, departureCity, sortType);
        }

        BookingReferenceStatusProcessor(driverAccountType, bookingReferences);
        bookingReferences = BookingReferenceProcessor(driverAccountType, user, bookingReferences);

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

        PageWrapper wrapper = PaginationProcessor(model, page, bookingReferences);

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

    private List<BookingReference> BookingReferenceProcessor(final String accountType, final User user, List<BookingReference> UserBookingReferences) {
        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
        for (final BookingReference reference: UserBookingReferences) {
            if (reference.getAccountType().equalsIgnoreCase(accountType)) {
                if (reference.getAuthor().equalsIgnoreCase(user.getUsername())) {
                    reference.setOwner(true);
                }
                if (!reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.CANCELLED) &&
                        !reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.COMPLETE) &&
                        !reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.EXPIRED) && reference.getPassengerNumber() != 0) {
                    bookingReferences.add(reference);
                }
            }
        }
        return bookingReferences;
    }

    private void BookingReferenceStatusProcessor(final String accountType, List<BookingReference> UserBookingReferences) {
        Date today = new Date();
        for (final BookingReference reference: UserBookingReferences) {
            if (reference.getAccountType().equalsIgnoreCase(accountType)) {
                if (reference.getDate().before(today)) {
                    if (reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
                        reference.setBookingStatus(BookingReferenceStatus.EXPIRED);
                        bookingService.saveBooking(reference);
                    } else if (reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS)) {
                        reference.setBookingStatus(BookingReferenceStatus.COMPLETE);
                        bookingService.saveBooking(reference);
                    }
                }
            }
        }
    }

    private String AddressCityProcessor(final String address) {
        final String finalAddress;
        if (address.contains(",")) {
            finalAddress = address.substring(0, address.indexOf(","));
        } else {
            finalAddress = address;
        }
        return finalAddress;
    }

    private PageWrapper PaginationProcessor(Model model, final Optional<Integer> page, List<BookingReference> bookingReferences) {
        final int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Pageable pageable = new PageRequest(evalPage, PAGE_SIZE);
        final int start = pageable.getOffset();
        final int end = (start + pageable.getPageSize()) > bookingReferences.size() ? bookingReferences.size() : (start + pageable.getPageSize());
        Page<BookingReference> pagedReferences = new PageImpl<BookingReference>(bookingReferences.subList(start, end), pageable, bookingReferences.size());
        PageWrapper wrapper = new PageWrapper(pagedReferences.getTotalPages(), pagedReferences.getNumber(), BUTTONS_TO_SHOW);
        model.addAttribute("bookingReferences", pagedReferences);
        return wrapper;
    }

    private Sort getSortType (String sortMethod) {
        Sort sortType = null;
        switch(sortMethod) {
            case "date-asc" :
                sortType = sortByDateASC;
                break;
            case "date-desc" :
                sortType = sortByDateDESC;
                break;
            case "price-asc" :
                sortType = sortByPriceASC;
                break;
            case "price-desc" :
                sortType = sortByPriceDESC;
                break;
        }
        return sortType;
    }

}
