package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.*;
import com.hoffman.carpool.error.UServiceException;
import com.hoffman.carpool.service.BookingService;
import com.hoffman.carpool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    public String createRiderBookingPost(@ModelAttribute("booking") BookingReference bookingReference, @ModelAttribute("dateString") String source, Model model, Principal principal) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = null;

        try {
            if (!source.isEmpty() && source != null) {
                date = format.parse(source);
            }
        } catch (ParseException e) {
            throw new UServiceException("TXN_101","", "Date parse error", e);
        }
        bookingReference.setDate(date);
        bookingReference.setAccountType(riderAccountType);

        User user = userService.findByUsername(principal.getName());
        RiderAccount riderAccount = user.getRiderAccount();
        bookingReference.setRiderAccount(riderAccount);

        final String author = riderAccount.getUsername();
        bookingReference.setAuthor(author);
        bookingReference.setBookingStatus(BookingReferenceStatus.PENDING);

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
    public String createDriverBookingPost(@ModelAttribute("booking") BookingReference bookingReference, @ModelAttribute("dateString") String source, Model model, Principal principal) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = null;

        try {
            if (!source.isEmpty() && source != null) {
                date = format.parse(source);
            }
        } catch (ParseException e) {
            throw new UServiceException("TXN_101","", "Date parse error", e);
        }
        bookingReference.setDate(date);
        bookingReference.setAccountType(driverAccountType);

        User user = userService.findByUsername(principal.getName());
        DriverAccount driverAccount = user.getDriverAccount();
        bookingReference.setDriverAccount(driverAccount);

        final String author = driverAccount.getUsername();
        bookingReference.setAuthor(author);
        bookingReference.setBookingStatus(BookingReferenceStatus.PENDING);

        bookingService.createBooking(bookingReference);
        return "redirect:/userFront";
    }

    @RequestMapping(value="/info", method = RequestMethod.GET)
    public String getUserInfo(@RequestParam(value = "author") String author, Model model, Principal principal) {
        User user = userService.findByUsername(author);
        model.addAttribute("user", user);

        return "userInfo";
    }

    @RequestMapping(value= "/riderBooking/view", method = RequestMethod.GET)
    public String getRiderBookingView(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        final String author = bookingReference.getAuthor();
        User user = userService.findByUsername(author);
        DriverAccount driverAccount = bookingReference.getDriverAccount();

        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
            return "riderBookingAcceptPage";
        }

        if (driverAccount != null) {
            final String driverName = driverAccount.getUsername();
            if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS) && driverName.equalsIgnoreCase(principal.getName())) {
                return "riderBookingCompletePage";
            }
        }

        return "redirect:/account/riderAccount";
    }

    @RequestMapping(value= "/riderBooking/accept", method = RequestMethod.POST)
    public String acceptRiderBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        DriverAccount driverAccount = user.getDriverAccount();

        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        bookingReference.setBookingStatus(BookingReferenceStatus.IN_PROGRESS);
        bookingReference.setDriverAccount(driverAccount);
        model.addAttribute("bookingReference", bookingReference);

        bookingService.saveBooking(bookingReference);

        return "redirect:/account/riderAccount";
    }

    @RequestMapping(value= "/riderBooking/complete", method = RequestMethod.POST)
    public String completeRiderBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        bookingReference.setBookingStatus(BookingReferenceStatus.COMPLETE);
        model.addAttribute("bookingReference", bookingReference);

        bookingService.saveBooking(bookingReference);

        return "redirect:/account/riderAccount";
    }

    @RequestMapping(value= "/riderBooking/author/view", method = RequestMethod.GET)
    public String getRiderBookingAuthorView(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);

        final String author = bookingReference.getAuthor();
        User user = userService.findByUsername(author);
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
            return "riderBookingCancelPendingPage";
        } else if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS)) {
            return "riderBookingCancelProgressPage";
        } else {
            return "redirect:/account/riderAccount";
        }
    }

    @RequestMapping(value= "/riderBooking/cancel", method = RequestMethod.POST)
    public String cancelRiderBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        bookingReference.setBookingStatus(BookingReferenceStatus.CANCELLED);
        model.addAttribute("bookingReference", bookingReference);

        bookingService.saveBooking(bookingReference);

        return "redirect:/account/riderAccount";
    }

    @RequestMapping(value= "/driverBooking/view", method = RequestMethod.GET)
    public String getDriverBookingView(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        final String author = bookingReference.getAuthor();
        User user = userService.findByUsername(author);
        RiderAccount riderAccount = bookingReference.getRiderAccount();

        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
            return "driverBookingAcceptPage";
        }

        if (riderAccount != null) {
            final String driverName = riderAccount.getUsername();
            if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS) && driverName.equalsIgnoreCase(principal.getName())) {
                return "driverBookingCancelProgressPage";
            }
        }

        return "redirect:/account/driverAccount";
    }

    @RequestMapping(value= "/driverBooking/accept", method = RequestMethod.POST)
    public String acceptDriverBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        User user = userService.findByUsername(principal.getName());
        RiderAccount riderAccount = user.getRiderAccount();

        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        bookingReference.setBookingStatus(BookingReferenceStatus.IN_PROGRESS);
        bookingReference.setRiderAccount(riderAccount);
        model.addAttribute("bookingReference", bookingReference);

        bookingService.saveBooking(bookingReference);

        return "redirect:/account/driverAccount";
    }

    @RequestMapping(value= "/driverBooking/complete", method = RequestMethod.POST)
    public String completeDriverBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        bookingReference.setBookingStatus(BookingReferenceStatus.COMPLETE);
        model.addAttribute("bookingReference", bookingReference);

        bookingService.saveBooking(bookingReference);

        return "redirect:/account/driverAccount";
    }

    @RequestMapping(value= "/driverBooking/author/view", method = RequestMethod.GET)
    public String getDriverBookingAuthorView(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);

        final String author = bookingReference.getAuthor();
        User user = userService.findByUsername(author);
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
            return "driverBookingCancelPendingPage";
        } else if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS)) {
            return "driverBookingCompletePage";
        } else {
            return "redirect:/account/driverAccount";
        }
    }

    @RequestMapping(value= "/driverBooking/cancel", method = RequestMethod.POST)
    public String cancelDriverBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        bookingReference.setBookingStatus(BookingReferenceStatus.CANCELLED);
        model.addAttribute("bookingReference", bookingReference);

        bookingService.saveBooking(bookingReference);

        return "redirect:/account/driverAccount";
    }
}
