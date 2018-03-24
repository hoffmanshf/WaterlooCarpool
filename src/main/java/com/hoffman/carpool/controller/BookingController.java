package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.*;
import com.hoffman.carpool.error.UServiceException;
import com.hoffman.carpool.service.BookingService;
import com.hoffman.carpool.service.EmailNotificationService;
import com.hoffman.carpool.util.DateTimeConverterUtil;
import com.hoffman.carpool.util.GoogleDistanceMatrixUtil;
import com.hoffman.carpool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("booking")
public class BookingController {

    private static final String riderAccountType = "Passenger";
    private static final String driverAccountType = "Driver";
    private static final String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
    private static final String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Autowired
    private GoogleDistanceMatrixUtil googleDistanceMatrixUtil;

    @Autowired
    private DateTimeConverterUtil dateTimeConverterUtil;

    @RequestMapping(value = "/riderCreate",method = RequestMethod.GET)
    public String createRiderBooking(Model model) {
        BookingReference bookingReference = new BookingReference();
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("dateString", "");

        return "riderBooking";
    }

    @RequestMapping(value = "/riderCreate",method = RequestMethod.POST)
    public String createRiderBookingPost(@ModelAttribute("booking") BookingReference bookingReference, @ModelAttribute("dateString") String source, Model model, Principal principal) {

        Date date = dateTimeConverterUtil.StringToDateConverter(source);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String month = monthNames[calendar.get(Calendar.MONTH)];
        String dayOfWeek = dayNames[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        String dayOfMonth = new Integer(calendar.get(Calendar.DAY_OF_MONTH)).toString();

        String time = dateTimeConverterUtil.DateToTimeConverter(date);

        bookingReference.setDayOfMonth(dayOfMonth);
        bookingReference.setDayOfWeek(dayOfWeek);
        bookingReference.setMonth(month);
        bookingReference.setDate(date);
        bookingReference.setTime(time);
        bookingReference.setAccountType(riderAccountType);

        User user = userService.findByUsername(principal.getName());
        RiderAccount riderAccount = user.getRiderAccount();
        List<RiderAccount> passengerList = new ArrayList<RiderAccount>();
        passengerList.add(riderAccount);
        bookingReference.setPassengerList(passengerList);

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
    public String createDriverBookingPost(@ModelAttribute("booking") BookingReference bookingReference, @ModelAttribute("dateString") String source,
                                          @RequestParam(value = "departure") String departure, @RequestParam(value = "arrival") String arrival, Principal principal) {

        Date date = dateTimeConverterUtil.StringToDateConverter(source);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String month = monthNames[calendar.get(Calendar.MONTH)];
        String dayOfWeek = dayNames[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        String dayOfMonth = new Integer(calendar.get(Calendar.DAY_OF_MONTH)).toString();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateForSearch = formatter.format(date);

        String time = dateTimeConverterUtil.DateToTimeConverter(date);

        bookingReference.setDayOfMonth(dayOfMonth);
        bookingReference.setDayOfWeek(dayOfWeek);
        bookingReference.setMonth(month);
        bookingReference.setDate(date);
        bookingReference.setTime(time);
        bookingReference.setDateForSearch(dateForSearch);
        bookingReference.setAccountType(driverAccountType);

        User user = userService.findByUsername(principal.getName());
        DriverAccount driverAccount = user.getDriverAccount();
        bookingReference.setDriverAccount(driverAccount);

        final String author = driverAccount.getUsername();
        bookingReference.setAuthor(author);
        bookingReference.setBookingStatus(BookingReferenceStatus.PENDING);

        bookingService.createBooking(bookingReference);
        googleDistanceMatrixUtil.estimateRouteTime(departure, arrival, calendar, bookingReference);
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

    @RequestMapping(value= "/driverBooking/view", method = RequestMethod.GET)
    public String getDriverBookingView(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        final String author = bookingReference.getAuthor();
        User user = userService.findByUsername(author);

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

        User user = userService.findByUsername(principal.getName());
        RiderAccount riderAccount = user.getRiderAccount();

        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        int availableSeats = bookingReference.getPassengerNumber();
        availableSeats -= seatsReserved;
        bookingReference.setPassengerNumber(availableSeats);
        List<RiderAccount> passengerList = bookingReference.getPassengerList();
        for (int i = 0; i < seatsReserved; ++i) {
            passengerList.add(riderAccount);
        }
        bookingReference.setPassengerList(passengerList);
        model.addAttribute("bookingReference", bookingReference);

        bookingReference.setBookingStatus(BookingReferenceStatus.IN_PROGRESS);

        bookingService.saveBooking(bookingReference);
        redirectAttributes.addAttribute("bookingReferenceId", bookingReferenceId);

        GregorianCalendar departureCalendar = new GregorianCalendar();
        departureCalendar.setTime(bookingReference.getDate());

        StringBuilder summary = new StringBuilder();
        summary.append("From ");
        summary.append(bookingReference.getDeparture());
        summary.append(" To ");
        summary.append(bookingReference.getArrival());

        CalendarEvent calendarEvent = new CalendarEvent("Waterloo Carpool", departureCalendar, bookingReference.getArrivalTime(), summary.toString(), "notification", "1");

        emailNotificationService.sendNotification(user.getEmail(), calendarEvent);

        return "redirect:/booking/driverBooking/success";
    }

    @RequestMapping(value= "/driverBooking/success", method = RequestMethod.GET)
    public String driverBookingSuccess(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        model.addAttribute("bookingReference", bookingReference);
        model.addAttribute("user", user);

        return "BookingSuccessPage";
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
            List<RiderAccount> passengers = bookingReference.getPassengerList();
            List<RiderAccount> distinctPassengers = passengers.stream()
                    .distinct()
                    .collect(Collectors.toList());
            int seatsOccupied = 0;
            List<BookingReferenceReservation> reservations = new ArrayList<>();
            for (RiderAccount distinctPassenger: distinctPassengers) {
                BookingReferenceReservation reservation = new BookingReferenceReservation();
                for (RiderAccount passenger: passengers) {
                    if (distinctPassenger.getUsername().equalsIgnoreCase(passenger.getUsername())) {
                        seatsOccupied += 1;
                    }
                }
                reservation.setPassenger(distinctPassenger);
                reservation.setSeatsOccupied(seatsOccupied);
                reservations.add(reservation);
            }
            model.addAttribute("reservations", reservations);
            return "driverBookingProgressPage";
        } else {
            return "redirect:/user/booking/driver";
        }
    }

    @RequestMapping(value= "/driverBooking/cancel", method = RequestMethod.POST)
    public String cancelDriverBooking(@RequestParam(value = "bookingReferenceId") Long bookingReferenceId, @RequestParam(value = "cancelNotes") String cancelNotes, Model model) {
        BookingReference bookingReference = bookingService.findBookingReference(bookingReferenceId);
        bookingReference.setBookingStatus(BookingReferenceStatus.CANCELLED);
        bookingReference.setCancelNotes(cancelNotes);
        model.addAttribute("bookingReference", bookingReference);

        bookingService.saveBooking(bookingReference);

        return "redirect:/user/booking/driver";
    }
}
