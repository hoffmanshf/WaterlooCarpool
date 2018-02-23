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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("booking")
public class BookingController {

    private static final String riderAccountType = "riderAccount";
    private static final String driverAccountType = "driverAccount";

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/riderCreate",method = RequestMethod.GET)
    public String createRiderBooking(Model model) {
        BookingReference bookingReference = new BookingReference();
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("dateString", "");

        return "riderBooking";
    }

    @RequestMapping(value = "/riderCreate",method = RequestMethod.POST)
    public String createRiderBookingPost(@ModelAttribute("booking") BookingReference bookingReference, @ModelAttribute("dateString") String date, Model model, Principal principal) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date d1 = format.parse( date );
        bookingReference.setDate(d1);
        bookingReference.setAccountType(riderAccountType);

        User user = userService.findByUsername(principal.getName());
        RiderAccount riderAccount = user.getRiderAccount();
        bookingReference.setRiderAccount(riderAccount);

        final String author = riderAccount.getUsername();
        bookingReference.setAuthor(author);

        bookingService.createBooking(bookingReference);

        return "redirect:/userFront";
    }

    @RequestMapping(value = "/driverCreate",method = RequestMethod.GET)
    public String createDriverBooking(Model model) {
        BookingReference bookingReference = new BookingReference();
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("dateString", "");

        return "driverBooking";
    }

    @RequestMapping(value = "/driverCreate",method = RequestMethod.POST)
    public String createDriverBookingPost(@ModelAttribute("booking") BookingReference bookingReference, @ModelAttribute("dateString") String date, Model model, Principal principal) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date d1 = format.parse( date );
        bookingReference.setDate(d1);
        bookingReference.setAccountType(driverAccountType);

        User user = userService.findByUsername(principal.getName());
        DriverAccount driverAccount = user.getDriverAccount();
        bookingReference.setDriverAccount(driverAccount);

        final String author = driverAccount.getUsername();
        bookingReference.setAuthor(author);

        bookingService.createBooking(bookingReference);
        return "redirect:/userFront";
    }

    @RequestMapping(value="/info", method = RequestMethod.GET)
    public String getUserInfo(@RequestParam(value = "author") String author, Model model, Principal principal) {
        User user = userService.findByUsername(author);
        model.addAttribute("user", user);

        return "userInfo";
    }
}
