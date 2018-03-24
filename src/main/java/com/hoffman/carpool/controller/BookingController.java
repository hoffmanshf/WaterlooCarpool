package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.*;
import com.hoffman.carpool.service.BookingService;
import com.hoffman.carpool.util.GoogleDistanceMatrixUtil;
import com.hoffman.carpool.service.UserService;
import com.hoffman.carpool.util.ReservationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.*;

@Controller
@RequestMapping("booking")
public class BookingController {

    private static final String riderAccountType = "Passenger";
    private static final String driverAccountType = "Driver";

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private GoogleDistanceMatrixUtil googleDistanceMatrixUtil;

    @Autowired
    private ReservationUtil reservationUtil;

    @RequestMapping(value = "/riderCreate",method = RequestMethod.GET)
    public String createRiderBooking(Model model) {
        BookingReference bookingReference = new BookingReference();
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("dateString", "");

        return "riderBooking";
    }

    @RequestMapping(value = "/riderCreate",method = RequestMethod.POST)
    public String createRiderBookingPost(@ModelAttribute("booking") BookingReference bookingReference, @ModelAttribute("dateString") String source,
                                         @RequestParam(value = "departure") String departure, @RequestParam(value = "arrival") String arrival, Principal principal) {

        bookingService.createBooking(bookingReference, source, riderAccountType, principal);
        googleDistanceMatrixUtil.estimateRouteTime(departure, arrival, source, bookingReference);

        return "redirect:/userFront";
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

        return "redirect:/user/booking/rider";
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
            return "redirect:/user/booking/rider";
        }
    }

    @RequestMapping(value= "/riderBooking/cancel", method = RequestMethod.POST)
    public String cancelRiderBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        bookingReference.setBookingStatus(BookingReferenceStatus.CANCELLED);
        model.addAttribute("bookingReference", bookingReference);

        bookingService.saveBooking(bookingReference);

        return "redirect:/user/booking/rider";
    }

    @RequestMapping(value = "/driverCreate",method = RequestMethod.GET)
    public String createDriverBooking(Model model) {
        final BookingReference bookingReference = new BookingReference();
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("dateString", "");

        return "driverBooking";
    }

    @RequestMapping(value = "/driverCreate",method = RequestMethod.POST)
    public String createDriverBookingPost(@ModelAttribute("booking") BookingReference bookingReference, @ModelAttribute("dateString") String source,
                                          @RequestParam(value = "departure") String departure, @RequestParam(value = "arrival") String arrival, Principal principal) {

        bookingService.createBooking(bookingReference, source, driverAccountType, principal);
        googleDistanceMatrixUtil.estimateRouteTime(departure, arrival, source, bookingReference);
        return "redirect:/userFront";
    }

    @RequestMapping(value= "/driverBooking/view", method = RequestMethod.GET)
    public String getDriverBookingView(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model) {

        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        final String author = bookingReference.getAuthor();
        final User user = userService.findByUsername(author);

        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
            return "driverBookingAcceptPage";
        }

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS)) {
            return "driverBookingAcceptPage";
        }

        return "redirect:/user/booking/driver";
    }

    @RequestMapping(value= "/driverBooking/accept", method = RequestMethod.POST)
    public String acceptDriverBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal,
                                      @RequestParam(value = "seatsReserved") Integer seatsReserved, RedirectAttributes redirectAttributes) {

        final User user = userService.findByUsername(principal.getName());
        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        bookingService.acceptDriverBooking(user, seatsReserved, bookingReference);

        model.addAttribute("bookingReference", bookingReference);
        redirectAttributes.addAttribute("bookingReferenceId", bookingReferenceId);

        return "redirect:/booking/driverBooking/success";
    }

    @RequestMapping(value= "/driverBooking/success", method = RequestMethod.GET)
    public String driverBookingSuccess(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        final User user = userService.findByUsername(principal.getName());
        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        return "BookingSuccessPage";
    }


    @RequestMapping(value= "/driverBooking/complete", method = RequestMethod.POST)
    public String completeDriverBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        bookingReference.setBookingStatus(BookingReferenceStatus.COMPLETE);
        model.addAttribute("bookingReference", bookingReference);

        bookingService.saveBooking(bookingReference);

        return "redirect:/account/driverAccount";
    }

    @RequestMapping(value= "/driverBooking/author/view", method = RequestMethod.GET)
    public String getDriverBookingAuthorView(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);

        final String author = bookingReference.getAuthor();
        final User user = userService.findByUsername(author);
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
            return "driverBookingCancelPendingPage";
        } else if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS)) {
            final List<BookingReferenceReservation> reservations = reservationUtil.getBookingReservationList(bookingReference);
            model.addAttribute("reservations", reservations);
            return "driverBookingProgressPage";
        } else {
            return "redirect:/user/booking/driver";
        }
    }

    @RequestMapping(value= "/driverBooking/cancel", method = RequestMethod.POST)
    public String cancelDriverBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, @RequestParam(value = "cancelNotes") String cancelNotes, Model model) {
        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        bookingReference.setBookingStatus(BookingReferenceStatus.CANCELLED);
        bookingReference.setCancelNotes(cancelNotes);
        model.addAttribute("bookingReference", bookingReference);

        bookingService.saveBooking(bookingReference);

        return "redirect:/user/booking/driver";
    }
}
