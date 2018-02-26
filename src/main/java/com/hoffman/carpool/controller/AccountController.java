package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.BookingReference;
import com.hoffman.carpool.domain.DriverAccount;
import com.hoffman.carpool.domain.RiderAccount;
import com.hoffman.carpool.domain.User;
import com.hoffman.carpool.service.BookingService;
import com.hoffman.carpool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountController {

    private static final String riderAccountType = "riderAccount";
    private static final String driverAccountType = "driverAccount";

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @RequestMapping(value = "/riderAccount", method = RequestMethod.GET)
    public String riderAccount(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        RiderAccount riderAccount = user.getRiderAccount();
        List<BookingReference> UserBookingReferences = bookingService.findAll();
        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
        for (final BookingReference reference: UserBookingReferences) {
            if (reference.getAccountType().equalsIgnoreCase(riderAccountType)) {
                if (reference.getAuthor().equalsIgnoreCase(user.getUsername())) {
                    reference.setOwner(true);
                }
                bookingReferences.add(reference);
            }
        }
        model.addAttribute("riderAccount", riderAccount);
        model.addAttribute("bookingReferences", bookingReferences);

        return "riderAccount";
    }

    @RequestMapping(value = "/driverAccount", method = RequestMethod.GET)
    public String driverAccount(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        DriverAccount driverAccount = user.getDriverAccount();
        List<BookingReference> UserBookingReferences = bookingService.findAll();
        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
        for (final BookingReference reference: UserBookingReferences) {
            if (reference.getAccountType().equalsIgnoreCase(driverAccountType)) {
                bookingReferences.add(reference);
            }
        }
        model.addAttribute("driverAccount", driverAccount);
        model.addAttribute("bookingReferences", bookingReferences);
        return "driverAccount";
    }

}
