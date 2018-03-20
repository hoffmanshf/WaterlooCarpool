package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.*;
import com.hoffman.carpool.error.UServiceException;
import com.hoffman.carpool.service.BookingService;
import com.hoffman.carpool.service.CarService;
import com.hoffman.carpool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private static final String riderAccountType = "Passenger";
    private static final String driverAccountType = "Driver";

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private BookingService bookingService;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        Car car = user.getDriverAccount().getCar();

        model.addAttribute("user", user);
        model.addAttribute("car", car);

        return "profile";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public String profilePost(@ModelAttribute("user") User newUser, @ModelAttribute("car") Car car, Principal principal, Model model) {

        User user = userService.findByUsername(principal.getName());
        final String newUserEmail = newUser.getEmail();

        if (userService.checkEmailExists(newUserEmail)) {

            if (!user.getEmail().equalsIgnoreCase(newUserEmail)) {
                model.addAttribute("emailExists", true);
                return "profile";
            }

        }

        user.setEmail(newUser.getEmail());
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setPhone(newUser.getPhone());

        model.addAttribute("user", user);
        model.addAttribute("car", car);

        userService.saveUser(user);
        carService.createCar(car);

        model.addAttribute("success", true);

        return "profile";
    }

    @RequestMapping(value = "/photo", method = RequestMethod.GET)
    public String getPhoto(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        model.addAttribute("user", user);
        return "userPhoto";
    }

    @RequestMapping(value = "/photo", method = RequestMethod.POST)
    public String uploadPhoto(@ModelAttribute("user") User newUser, @RequestParam(value = "document") MultipartFile file, Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());

        if (!file.isEmpty()) {
            try {
                user.setUserPhoto(file.getBytes());
            } catch (IOException e) {
                throw new UServiceException("TXN_102","", "Photo parse error", e);
            }
        }

        model.addAttribute("user", user);
        userService.saveUser(user);
        return "userPhoto";
    }

    @RequestMapping(value = "/photo/viewByUsername", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImageByUserName(@RequestParam(value = "username") String username) {

        User user = userService.findByUsername(username);
        byte[] imageContent = user.getUserPhoto();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/photo/view", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> getImage(Principal principal) {

        User user = userService.findByUsername(principal.getName());
        byte[] imageContent = user.getUserPhoto();
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_PNG);
        return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
    }

    @RequestMapping(value = "/booking/rider", method = RequestMethod.GET)
    public String getRiderBookingHistory(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<BookingReference> bookingReferences = bookingService.findAll();
        BookingReferenceStatusProcessor(riderAccountType, bookingReferences);
        bookingReferences = RiderBookingReferenceProcessor(user, bookingReferences);
        model.addAttribute("user", user);
        model.addAttribute("bookingReferences", bookingReferences);
        return "riderBookingHistory";
    }

    @RequestMapping(value = "/booking/driver", method = RequestMethod.GET)
    public String getDriverBookingHistory(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<BookingReference> bookingReferences = bookingService.findAll();
        BookingReferenceStatusProcessor(driverAccountType, bookingReferences);
        bookingReferences = DriverBookingReferenceProcessor(user, bookingReferences);
        model.addAttribute("user", user);
        model.addAttribute("bookingReferences", bookingReferences);
        return "driverBookingHistory";
    }

    private List<BookingReference> RiderBookingReferenceProcessor(final User user, List<BookingReference> UserBookingReferences) {
        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
        for (final BookingReference reference: UserBookingReferences) {
            if (reference.getAuthor().equalsIgnoreCase(user.getUsername())) {
                reference.setOwner(true);
            }
            List<RiderAccount> accounts = reference.getPassengerList();
            if (accounts != null) {
                for (RiderAccount account : accounts) {
                    if (account.getUsername().equalsIgnoreCase(user.getUsername())) {
                        bookingReferences.add(reference);
                        break;
                    }
                }
            }
        }
        return bookingReferences;
    }

    private List<BookingReference> DriverBookingReferenceProcessor(final User user, List<BookingReference> UserBookingReferences) {
        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
        for (final BookingReference reference: UserBookingReferences) {
            if (reference.getAuthor().equalsIgnoreCase(user.getUsername())) {
                reference.setOwner(true);
            }
            if (reference.getDriverAccount() != null) {
                if (reference.getDriverAccount().getUsername().equalsIgnoreCase(user.getUsername())) {
                    bookingReferences.add(reference);
                    continue;
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

}
