//package com.hoffman.carpool.controller;
//
//import com.hoffman.carpool.domain.*;
//import com.hoffman.carpool.service.BookingService;
//import com.hoffman.carpool.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import java.security.Principal;
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//@RequestMapping("/schedule")
//public class ScheduleController {
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private BookingService bookingService;
//
//    @RequestMapping(value = "/ride", method = RequestMethod.GET)
//    public String riderSchedule(Model model, Principal principal) {
//        User user = userService.findByUsername(principal.getName());
//        RiderAccount riderAccount = user.getRiderAccount();
//        final String username = user.getUsername();
//
//        List<BookingReference> UserBookingReferences = bookingService.findAll();
//        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
//
//        for (final BookingReference reference: UserBookingReferences) {
//            if (reference.getRiderAccount() != null) {
//                if (reference.getRiderAccount().getUsername().equalsIgnoreCase(username)) {
//                    if (reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS) ||
//                            reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
//                        bookingReferences.add(reference);
//                    }
//                    if (reference.getAuthor().equalsIgnoreCase(user.getUsername())) {
//                        reference.setOwner(true);
//                    }
//                }
//            }
//        }
//
//        model.addAttribute("riderAccount", riderAccount);
//        model.addAttribute("bookingReferences", bookingReferences);
//        return "riderSchedule";
//    }
//
//    @RequestMapping(value = "/drive", method = RequestMethod.GET)
//    public String driverSchedule(Model model, Principal principal) {
//        User user = userService.findByUsername(principal.getName());
//        DriverAccount driverAccount = user.getDriverAccount();
//        final String username = user.getUsername();
//
//        List<BookingReference> UserBookingReferences = bookingService.findAll();
//        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
//
//        for (final BookingReference reference: UserBookingReferences) {
//            if (reference.getDriverAccount() != null) {
//                if (reference.getDriverAccount().getUsername().equalsIgnoreCase(username)) {
//                    if (reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS) ||
//                            reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
//                        bookingReferences.add(reference);
//                    }
//                    if (reference.getAuthor().equalsIgnoreCase(user.getUsername())) {
//                        reference.setOwner(true);
//                    }
//                }
//            }
//        }
//
//        model.addAttribute("driverAccount", driverAccount);
//        model.addAttribute("bookingReferences", bookingReferences);
//        return "driverSchedule";
//    }
//}
