package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.*;
import com.hoffman.carpool.domain.constant.AccountType;
import com.hoffman.carpool.domain.constant.BookingReferenceStatus;
import com.hoffman.carpool.domain.entity.BookingReference;
import com.hoffman.carpool.domain.entity.Notification;
import com.hoffman.carpool.domain.entity.RiderAccount;
import com.hoffman.carpool.domain.entity.User;
import com.hoffman.carpool.service.BookingService;
import com.hoffman.carpool.service.NotificationService;
import com.hoffman.carpool.util.GoogleDistanceMatrixUtil;
import com.hoffman.carpool.service.UserService;
import com.hoffman.carpool.util.ReservationUtil;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.groovy.util.StringUtil;
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

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private GoogleDistanceMatrixUtil googleDistanceMatrixUtil;

    @Autowired
    private ReservationUtil reservationUtil;

    @RequestMapping(value = "/riderCreate",method = RequestMethod.GET)
    public String createRiderBooking(Model model, Principal principal) {
        final User user = userService.findByUsername(principal.getName());
        final BookingReference bookingReference = new BookingReference();
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("dateString", "");
        model.addAttribute("user", user);

        return "riderBooking";
    }

    @RequestMapping(value = "/riderCreate",method = RequestMethod.POST)
    public String createRiderBookingPost(@ModelAttribute("bookingReference") BookingReference bookingReference,
                                         @ModelAttribute("dateString") String dateSource,
                                         @ModelAttribute("backDateString") String backDateSource,
                                         @RequestParam(value = "departure") String departure,
                                         @RequestParam(value = "arrival") String arrival,
                                         @RequestParam(value = "passengerNumber", required = false) int passengerNumber,
                                         @RequestParam(value = "returnPassengerNumber", required = false) int returnPassengerNumber,
                                         Principal principal, RedirectAttributes redirectAttributes) {
        bookingReference.setPassengerNumber(passengerNumber);
        bookingService.createBooking(bookingReference, dateSource, AccountType.riderAccountType, principal);
        googleDistanceMatrixUtil.estimateRouteTime(departure, arrival, dateSource, bookingReference);
        redirectAttributes.addAttribute("bookingReferenceId", bookingReference.getBookingReferenceId());

        if (backDateSource != null && StringUtils.isNotEmpty(backDateSource)) {
            BookingReference secondBookingReference = new BookingReference();
            secondBookingReference.setPassengerNumber(returnPassengerNumber);
            secondBookingReference.setArrival(bookingReference.getDeparture());
            secondBookingReference.setDeparture(bookingReference.getArrival());
            secondBookingReference.setNotes(bookingReference.getNotes());
            bookingService.createBooking(secondBookingReference, backDateSource, AccountType.riderAccountType, principal);
            googleDistanceMatrixUtil.estimateRouteTime(arrival, departure, backDateSource, secondBookingReference);
            redirectAttributes.addAttribute("bookingReferenceId2", secondBookingReference.getBookingReferenceId());
        }

        return "redirect:/booking/riderBooking/submitted";
    }

    @RequestMapping(value = "/riderBooking/submitted",method = RequestMethod.GET)
    public String riderBookingSubmitted(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId,
                                        @RequestParam(value = "bookingReferenceId2", required = false) Long bookingReferenceId2,
                                        Model model, Principal principal) {

        final User user = userService.findByUsername(principal.getName());
        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        model.addAttribute("bookingReference", bookingReference);
        if (bookingReferenceId2 != null) {
            final BookingReference bookingReference2 = bookingService.findBookingReference(bookingReferenceId2);
            model.addAttribute("bookingReference2", bookingReference2);
        }
        model.addAttribute("user", user);
        return "riderBookingSubmittedPage";
    }

    @RequestMapping(value= "/riderBooking/view", method = RequestMethod.GET)
    public String getRiderBookingView(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        User user = userService.findByUsername(principal.getName());

        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
            final RiderAccount riderAccount = bookingReference.getPassengerList().get(0);
            model.addAttribute("riderOwner", riderAccount);
            return "riderBookingAcceptPage";
        }

        return "redirect:/user/booking/rider";
    }

    @RequestMapping(value= "/riderBooking/accept", method = RequestMethod.POST)
    public String acceptRiderBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model,
                                     RedirectAttributes redirectAttributes, Principal principal) {

        final User user = userService.findByUsername(principal.getName());
        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        bookingService.acceptRiderBooking(user, bookingReference);

        model.addAttribute("bookingReference", bookingReference);
        redirectAttributes.addAttribute("bookingReferenceId", bookingReferenceId);
        notificationService.sendAcceptedNotification(bookingReference, user);

        return "redirect:/booking/riderBooking/success";
    }

    @RequestMapping(value= "/riderBooking/success", method = RequestMethod.GET)
    public String riderBookingSuccess(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        final User user = userService.findByUsername(principal.getName());
        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        return "riderBookingSuccessPage";
    }

    @RequestMapping(value= "/riderBooking/author/view", method = RequestMethod.GET)
    public String getRiderBookingAuthorView(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        final User user = userService.findByUsername(principal.getName());
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
            final RiderAccount riderAccount = bookingReference.getPassengerList().get(0);
            model.addAttribute("riderOwner", riderAccount);
            return "riderBookingCancelPendingPage";
        } else if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS)) {
            final List<BookingReferenceReservation> reservations = reservationUtil.getBookingReservationList(bookingReference);
            model.addAttribute("reservations", reservations);
            return "riderBookingProgressPage";
        } else if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.CANCELLED)) {
            return "riderBookingCancelledPage";
        } else if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.COMPLETE)) {
            return "riderBookingCompletePage";
        } else if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.EXPIRED)) {
            return "riderBookingExpiredPage";
        }
        return "redirect:/user/booking/rider";
    }

    @RequestMapping(value= "/riderBooking/cancel", method = RequestMethod.POST)
    public String cancelRiderBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        final User user = userService.findByUsername(principal.getName());
        bookingReference.setBookingStatus(BookingReferenceStatus.CANCELLED);
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        bookingService.saveBooking(bookingReference);
        if (!bookingReference.getAuthor().equalsIgnoreCase(user.getUsername())) {
            notificationService.sendCancelledNotification(bookingReference, user);
        }

        return "riderBookingCancelledPage";
    }

    @RequestMapping(value= "/riderBooking/passenger/cancel", method = RequestMethod.POST)
    public String cancelPassengerBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        final User user = userService.findByUsername(principal.getName());
        int seatsOffset = 0;
        List<RiderAccount> passengers = bookingReference.getPassengerList();
        List<RiderAccount> cancelledPassengers = bookingReference.getCancelledPassengerList();
        for (Iterator<RiderAccount> iterator = passengers.iterator(); iterator.hasNext();) {
            String passengerName = iterator.next().getUsername();
            if (passengerName.equalsIgnoreCase(user.getUsername())) {
                seatsOffset += 1;
                iterator.remove();
            }
        }
        cancelledPassengers.add(user.getRiderAccount());
        int seatsLeft = bookingReference.getPassengerNumber();
        seatsLeft +=seatsOffset;
        bookingReference.setCancelledPassengerList(cancelledPassengers);
        bookingReference.setPassengerNumber(seatsLeft);
        bookingReference.setBookingStatus(BookingReferenceStatus.BACK_PENDING);
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        bookingService.saveBooking(bookingReference);
        notificationService.sendCancelledNotification(bookingReference, user);

        return "riderBookingCancelledPage";
    }

    @RequestMapping(value = "/driverCreate",method = RequestMethod.GET)
    public String createDriverBooking(Model model, Principal principal) {
        final BookingReference bookingReference = new BookingReference();
        final User user = userService.findByUsername(principal.getName());
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("dateString", "");
        model.addAttribute("user", user);

        return "driverBooking";
    }

    @RequestMapping(value = "/driverCreate",method = RequestMethod.POST)
    public String createDriverBookingPost(@ModelAttribute("bookingReference") BookingReference bookingReference, @ModelAttribute("dateString") String source,
                                          @RequestParam(value = "passengerNumber", required = false) int passengerNumber,
                                          @RequestParam(value = "price", required = false) int price,
                                          @RequestParam(value = "departure") String departure, @RequestParam(value = "arrival") String arrival, Principal principal, RedirectAttributes redirectAttributes) {

        bookingReference.setPassengerNumber(passengerNumber);
        bookingReference.setPrice(price);
        bookingService.createBooking(bookingReference, source, AccountType.driverAccountType, principal);
        googleDistanceMatrixUtil.estimateRouteTime(departure, arrival, source, bookingReference);
        redirectAttributes.addAttribute("bookingReferenceId", bookingReference.getBookingReferenceId());

        return "redirect:/booking/driverBooking/submitted";
    }

    @RequestMapping(value = "/driverBooking/submitted", method = RequestMethod.GET)
    public String driverBookingSubmitted(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        final User user = userService.findByUsername(principal.getName());
        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);
        return "driverBookingSubmittedPage";
    }

    @RequestMapping(value= "/driverBooking/view", method = RequestMethod.GET)
    public String getDriverBookingView(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        final User user = userService.findByUsername(principal.getName());

        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
            return "driverBookingAcceptPage";
        }

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS)) {
            return "driverBookingAcceptPage";
        }

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.BACK_PENDING)) {
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
        notificationService.sendAcceptedNotification(bookingReference, user);

        return "redirect:/booking/driverBooking/success";
    }

    @RequestMapping(value= "/driverBooking/success", method = RequestMethod.GET)
    public String driverBookingSuccess(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        final User user = userService.findByUsername(principal.getName());
        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        return "driverBookingSuccessPage";
    }

    @RequestMapping(value= "/driverBooking/author/view", method = RequestMethod.GET)
    public String getDriverBookingAuthorView(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        final User user = userService.findByUsername(principal.getName());
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
            return "driverBookingCancelPendingPage";
        } else if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.BACK_PENDING) &&
                bookingReference.getPassengerList().size() == 0) {
            return "driverBookingBackPendingPage";
        } else if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS) ||
                bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.BACK_PENDING)) {
            final List<BookingReferenceReservation> reservations = reservationUtil.getBookingReservationList(bookingReference);
            model.addAttribute("reservations", reservations);
            return "driverBookingProgressPage";
        } else if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.CANCELLED)) {
            return "driverBookingCancelledPage";
        } else if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.COMPLETE)) {
            return "driverBookingCompletePage";
        } else if (bookingReference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.EXPIRED)) {
            return "driverBookingExpiredPage";
        }
        return "redirect:/user/booking/driver";
    }

    @RequestMapping(value= "/driverBooking/cancel", method = RequestMethod.GET)
    public String getDriverBookingBackPending(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {

        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        final User user = userService.findByUsername(principal.getName());
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        return "driverBookingCancelPendingPage";
    }

    @RequestMapping(value= "/driverBooking/cancel", method = RequestMethod.POST)
    public String cancelDriverBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        final BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        final User user = userService.findByUsername(principal.getName());
        bookingReference.setBookingStatus(BookingReferenceStatus.CANCELLED);
//        bookingReference.setCancelNotes(cancelNotes);
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        bookingService.saveBooking(bookingReference);
        if (!bookingReference.getAuthor().equalsIgnoreCase(user.getUsername())) {
            notificationService.sendCancelledNotification(bookingReference, user);
        }

        return "driverBookingCancelledPage";
    }
}
