package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.*;
import com.hoffman.carpool.service.AccountService;
import com.hoffman.carpool.service.UserService;
import com.hoffman.carpool.domain.PageWrapper;
import com.hoffman.carpool.util.PaginationUtil;
import com.hoffman.carpool.util.URIBuilderUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.URI;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class AccountController {

    private static final int BUTTONS_TO_SHOW = 5;

    @Autowired
    private UserService userService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private PaginationUtil paginationUtil;

    @Autowired
    private URIBuilderUtil uriBuilderUtil;

    @RequestMapping(value = "/riderAccount", method = RequestMethod.GET)
    public String riderAccount(Model model, Principal principal,
                               @RequestParam(value = "sort", required = false) String sort,
                               @RequestParam(value = "page") Optional<Integer> page) {

        final User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        final RiderAccount riderAccount = user.getRiderAccount();

        if (sort == null) {
            final URI paginationURI = uriBuilderUtil.getAccountURI(AccountBaseURL.riderAccountBaseURL);
            model.addAttribute("paginationURI", paginationURI);
            sort = "date-asc";
        } else {
            final URI paginationURI = uriBuilderUtil.getAccountURI(AccountBaseURL.riderAccountBaseURL, sort);
            model.addAttribute("paginationURI", paginationURI);
        }

        final URI sortingURI = uriBuilderUtil.getAccountURI(AccountBaseURL.riderAccountBaseURL);
        final List<BookingReference> bookingReferences = accountService.getAccountBookingReference(sort, AccountType.riderAccountType, user);

        if (bookingReferences.size() == 0) {
            return "riderAccountNoResult";
        }

        final Page<BookingReference> pagedReferences = paginationUtil.getPagedReferences(page, bookingReferences);
        final PageWrapper wrapper = new PageWrapper(pagedReferences.getTotalPages(), pagedReferences.getNumber(), BUTTONS_TO_SHOW);

        model.addAttribute("sortingURI", sortingURI);
        model.addAttribute("riderAccount", riderAccount);
        model.addAttribute("bookingReferences", pagedReferences);
        model.addAttribute("wrapper", wrapper);

        return "riderAccount";
    }

    @RequestMapping(value = "/driverAccount", method = RequestMethod.GET)
    public String driverAccount(Model model, Principal principal,
                                @RequestParam(value = "sort", required = false) String sort,
                                @RequestParam(value = "page") Optional<Integer> page) {

        final User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);

        final DriverAccount driverAccount = user.getDriverAccount();

        if (sort == null) {
            final URI paginationURI = uriBuilderUtil.getAccountURI(AccountBaseURL.driverAccountBaseURL);
            model.addAttribute("paginationURI", paginationURI);
            sort = "date-asc";
        } else {
            final URI paginationURI = uriBuilderUtil.getAccountURI(AccountBaseURL.driverAccountBaseURL, sort);
            model.addAttribute("paginationURI", paginationURI);
        }

        final URI sortingURI = uriBuilderUtil.getAccountURI(AccountBaseURL.driverAccountBaseURL);
        final List<BookingReference> bookingReferences = accountService.getAccountBookingReference(sort, AccountType.driverAccountType, user);

        if (bookingReferences.size() == 0) {
            return "driverAccountNoResult";
        }

        final Page<BookingReference> pagedReferences = paginationUtil.getPagedReferences(page, bookingReferences);
        final PageWrapper wrapper = new PageWrapper(pagedReferences.getTotalPages(), pagedReferences.getNumber(), BUTTONS_TO_SHOW);

        model.addAttribute("sortingURI", sortingURI);
        model.addAttribute("driverAccount", driverAccount);
        model.addAttribute("bookingReferences", pagedReferences);
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

        final User user = userService.findByUsername(principal.getName());
        final DriverAccount driverAccount = user.getDriverAccount();

        if (sort == null) {
            final URI paginationURI = uriBuilderUtil.getAccountSearchURI(AccountBaseURL.driverAccountSearchBaseURL, arrival, departure, date);
            model.addAttribute("paginationURI", paginationURI);
            sort = "date-asc";
        } else {
            final URI paginationURI = uriBuilderUtil.getAccountSearchURI(AccountBaseURL.driverAccountSearchBaseURL, arrival, departure, date, sort);
            model.addAttribute("paginationURI", paginationURI);
        }

        final URI sortingURI = uriBuilderUtil.getAccountSearchURI(AccountBaseURL.driverAccountSearchBaseURL, arrival, departure, date);
        final List<BookingReference> bookingReferences = accountService.getAccountSearchResult(sort, AccountType.driverAccountType, date, arrival, departure, user);

        if (bookingReferences.size() == 0) {
            return "driverAccountNoResult";
        }

        final Page<BookingReference> pagedReferences = paginationUtil.getPagedReferences(page, bookingReferences);
        final PageWrapper wrapper = new PageWrapper(pagedReferences.getTotalPages(), pagedReferences.getNumber(), BUTTONS_TO_SHOW);

        model.addAttribute("sortingURI", sortingURI);
        model.addAttribute("driverAccount", driverAccount);
        model.addAttribute("bookingReferences", pagedReferences);
        model.addAttribute("wrapper", wrapper);
        model.addAttribute("user", user);

        return "driverAccount";
    }

    @RequestMapping(value = "/riderAccount/search", method = RequestMethod.GET)
    public String searchRiderBooking(@RequestParam(value = "arrival") String arrival,
                                      @RequestParam(value = "departure") String departure,
                                      @RequestParam(value = "date") String date,
                                      @RequestParam(value = "sort", required = false) String sort,
                                      @RequestParam(value = "page") Optional<Integer> page,
                                      Model model, Principal principal) {

        final User user = userService.findByUsername(principal.getName());
        final RiderAccount riderAccount = user.getRiderAccount();

        if (sort == null) {
            final URI paginationURI = uriBuilderUtil.getAccountSearchURI(AccountBaseURL.riderAccountSearchBaseURL, arrival, departure, date);
            model.addAttribute("paginationURI", paginationURI);
            sort = "date-asc";
        } else {
            final URI paginationURI = uriBuilderUtil.getAccountSearchURI(AccountBaseURL.riderAccountSearchBaseURL, arrival, departure, date, sort);
            model.addAttribute("paginationURI", paginationURI);
        }

        final URI sortingURI = uriBuilderUtil.getAccountSearchURI(AccountBaseURL.riderAccountSearchBaseURL, arrival, departure, date);
        final List<BookingReference> bookingReferences = accountService.getAccountSearchResult(sort, AccountType.riderAccountType, date, arrival, departure, user);

        if (bookingReferences.size() == 0) {
            return "riderAccountNoResult";
        }

        final Page<BookingReference> pagedReferences = paginationUtil.getPagedReferences(page, bookingReferences);
        final PageWrapper wrapper = new PageWrapper(pagedReferences.getTotalPages(), pagedReferences.getNumber(), BUTTONS_TO_SHOW);

        model.addAttribute("sortingURI", sortingURI);
        model.addAttribute("riderAccount", riderAccount);
        model.addAttribute("bookingReferences", pagedReferences);
        model.addAttribute("wrapper", wrapper);
        model.addAttribute("user", user);

        return "riderAccount";
    }

    @RequestMapping(value = "/driverAccount/searchPassenger", method = RequestMethod.GET)
    public String searchPassengerDriverBooking(@RequestParam(value = "arrival") String arrival,
                                               @RequestParam(value = "departure") String departure,
                                               @RequestParam(value = "date") String date,
                                               @RequestParam(value = "passengerNumber") String passengerNumber,
                                               @RequestParam(value = "sort", required = false) String sort,
                                               @RequestParam(value = "page") Optional<Integer> page,
                                               Model model, Principal principal) {

        final User user = userService.findByUsername(principal.getName());
        final DriverAccount driverAccount = user.getDriverAccount();

        if (sort == null) {
            final URI paginationURI = uriBuilderUtil.getAccountSearchPassengerURI(AccountBaseURL.driverAccountSearchPassengerBaseURL, arrival, departure, date, passengerNumber);
            model.addAttribute("paginationURI", paginationURI);
            sort = "date-asc";
        } else {
            final URI paginationURI = uriBuilderUtil.getAccountSearchPassengerURI(AccountBaseURL.driverAccountSearchPassengerBaseURL, arrival, departure, date, passengerNumber, sort);
            model.addAttribute("paginationURI", paginationURI);
        }

        final URI sortingURI = uriBuilderUtil.getAccountSearchPassengerURI(AccountBaseURL.driverAccountSearchPassengerBaseURL, arrival, departure, date, passengerNumber);
        final List<BookingReference> bookingReferences = accountService.getAccountSearchResult(sort, AccountType.driverAccountType, date, arrival, departure, user, passengerNumber);

        if (bookingReferences.size() == 0) {
            return "driverAccountNoResult";
        }

        final Page<BookingReference> pagedReferences = paginationUtil.getPagedReferences(page, bookingReferences);
        final PageWrapper wrapper = new PageWrapper(pagedReferences.getTotalPages(), pagedReferences.getNumber(), BUTTONS_TO_SHOW);

        model.addAttribute("sortingURI", sortingURI);
        model.addAttribute("driverAccount", driverAccount);
        model.addAttribute("bookingReferences", pagedReferences);
        model.addAttribute("wrapper", wrapper);
        model.addAttribute("user", user);

        return "driverAccount";
    }

    @RequestMapping(value = "/riderAccount/searchPassenger", method = RequestMethod.GET)
    public String searchPassengerRiderBooking(@RequestParam(value = "arrival") String arrival,
                                               @RequestParam(value = "departure") String departure,
                                               @RequestParam(value = "date") String date,
                                               @RequestParam(value = "passengerNumber") String passengerNumber,
                                               @RequestParam(value = "sort", required = false) String sort,
                                               @RequestParam(value = "page") Optional<Integer> page,
                                               Model model, Principal principal) {

        final User user = userService.findByUsername(principal.getName());
        final RiderAccount riderAccount = user.getRiderAccount();

        if (sort == null) {
            final URI paginationURI = uriBuilderUtil.getAccountSearchPassengerURI(AccountBaseURL.riderAccountSearchPassengerBaseURL, arrival, departure, date, passengerNumber);
            model.addAttribute("paginationURI", paginationURI);
            sort = "date-asc";
        } else {
            final URI paginationURI = uriBuilderUtil.getAccountSearchPassengerURI(AccountBaseURL.riderAccountSearchPassengerBaseURL, arrival, departure, date, passengerNumber, sort);
            model.addAttribute("paginationURI", paginationURI);
        }

        final URI sortingURI = uriBuilderUtil.getAccountSearchPassengerURI(AccountBaseURL.riderAccountSearchPassengerBaseURL, arrival, departure, date, passengerNumber);
        final List<BookingReference> bookingReferences = accountService.getAccountSearchResult(sort, AccountType.riderAccountType, date, arrival, departure, user, passengerNumber);

        if (bookingReferences.size() == 0) {
            return "riderAccountNoResult";
        }

        final Page<BookingReference> pagedReferences = paginationUtil.getPagedReferences(page, bookingReferences);
        final PageWrapper wrapper = new PageWrapper(pagedReferences.getTotalPages(), pagedReferences.getNumber(), BUTTONS_TO_SHOW);

        model.addAttribute("sortingURI", sortingURI);
        model.addAttribute("riderAccount", riderAccount);
        model.addAttribute("bookingReferences", pagedReferences);
        model.addAttribute("wrapper", wrapper);
        model.addAttribute("user", user);

        return "riderAccount";
    }

}
