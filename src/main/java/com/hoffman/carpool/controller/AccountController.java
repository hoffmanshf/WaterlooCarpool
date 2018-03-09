package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.*;
import com.hoffman.carpool.service.BookingService;
import com.hoffman.carpool.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/account")
public class AccountController {

    private static final String riderAccountType = "riderAccount";
    private static final String driverAccountType = "driverAccount";

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int INITIAL_PAGE = 0;
    private static final int PAGE_SIZE = 5;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @RequestMapping(value = "/riderAccount", method = RequestMethod.GET)
    public String riderAccount(Model model, Principal principal,
                               @RequestParam(value = "page") Optional<Integer> page) {
        User user = userService.findByUsername(principal.getName());
        RiderAccount riderAccount = user.getRiderAccount();
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC.ASC, "date"));
        List<BookingReference> bookingReferences = bookingService.findAll(sort);

        bookingReferences = BookingReferenceProcessor(riderAccountType, user, bookingReferences);
        model.addAttribute("riderAccount", riderAccount);
        model.addAttribute("bookingReferences", bookingReferences);

        return "riderAccount";
    }

    @RequestMapping(value = "/driverAccount", method = RequestMethod.GET)
    public String driverAccount(Model model, Principal principal,
                                @RequestParam("page") Optional<Integer> page) {
        User user = userService.findByUsername(principal.getName());
        DriverAccount driverAccount = user.getDriverAccount();
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC.ASC, "date"));
        List<BookingReference> bookingReferences = bookingService.findAll(sort);
        bookingReferences = BookingReferenceProcessor(driverAccountType, user, bookingReferences);

        PageWrapper wrapper = PaginationProcessor(model, page, bookingReferences);

        model.addAttribute("driverAccount", driverAccount);
        model.addAttribute("wrapper", wrapper);
        return "driverAccount";
    }

    @RequestMapping(value = "/driverAccount/search", method = RequestMethod.GET)
    public String searchDriverBooking(@RequestParam(value = "arrival") String arrival,
                                      @RequestParam(value = "departure") String departure,
                                      @RequestParam(value = "date") String date,
                                      @RequestParam(value = "page") Optional<Integer> page,
                                      Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        DriverAccount driverAccount = user.getDriverAccount();
        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
        Sort sortByDate = new Sort(new Sort.Order(Sort.Direction.ASC.ASC, "date"));
        Sort sortByPrice = new Sort(new Sort.Order(Sort.Direction.ASC.ASC, "price"));


        // arrival and departure are encoded in frontend and automatically decoded in backend
        if (date != null && StringUtils.isNotEmpty(date)) {
            bookingReferences = bookingService.searchBookingReference(arrival, departure, date, sortByPrice);
        } else {
            bookingReferences = bookingService.searchBookingReferenceWithoutDate(arrival, departure, sortByDate);
        }

        bookingReferences = BookingReferenceProcessor(driverAccountType, user, bookingReferences);
        model.addAttribute("bookingReferences", bookingReferences);

        PageWrapper wrapper = PaginationProcessor(model, page, bookingReferences);

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
                                               @RequestParam(value = "page") Optional<Integer> page,
                                               Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        DriverAccount driverAccount = user.getDriverAccount();
        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
        Sort sortByDate = new Sort(new Sort.Order(Sort.Direction.ASC.ASC, "date"));
        Sort sortByPrice = new Sort(new Sort.Order(Sort.Direction.ASC.ASC, "price"));

        // arrival and departure are encoded in frontend and automatically decoded in backend
        if (date != null && StringUtils.isNotEmpty(date)) {
            bookingReferences = bookingService.searchBookingReference(arrival, departure, date, sortByPrice);
        } else {
            bookingReferences = bookingService.searchBookingReferenceWithoutDate(arrival, departure, sortByDate);
        }

        bookingReferences = BookingReferenceProcessor(driverAccountType, user, bookingReferences);

        if (passengerNumber != null && StringUtils.isNotEmpty(passengerNumber)) {
            final int seats = Integer.valueOf(passengerNumber);
            List<BookingReference> filteredBookingReferences = new ArrayList<BookingReference>();
            for (final BookingReference reference: bookingReferences) {
                if (seats <= reference.getPassengerNumber()) {
                    filteredBookingReferences.add(reference);
                }
            }
            model.addAttribute("bookingReferences", filteredBookingReferences);
        } else {
            model.addAttribute("bookingReferences", bookingReferences);
        }

        PageWrapper wrapper = PaginationProcessor(model, page, bookingReferences);

        model.addAttribute("wrapper", wrapper);
        model.addAttribute("driverAccount", driverAccount);
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
                        reference.getPassengerNumber() != 0) {
                    bookingReferences.add(reference);
                }
            }
        }
        return bookingReferences;
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

}
