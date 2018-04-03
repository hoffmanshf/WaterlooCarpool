package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.constant.AccountType;
import com.hoffman.carpool.domain.entity.BookingReference;
import com.hoffman.carpool.domain.entity.Car;
import com.hoffman.carpool.domain.entity.Notification;
import com.hoffman.carpool.domain.entity.User;
import com.hoffman.carpool.error.UServiceException;
import com.hoffman.carpool.service.BookingService;
import com.hoffman.carpool.service.CarService;
import com.hoffman.carpool.service.NotificationService;
import com.hoffman.carpool.service.UserService;
import com.hoffman.carpool.util.BookingReferenceUtil;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.List;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private BookingReferenceUtil bookingReferenceUtil;

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

    @RequestMapping(value = "/notification", method = RequestMethod.DELETE)
    public String deleteNotification(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        List<Notification> notificationList = user.getNotificationList();
        if (notificationList != null) {
            for (Notification notification : notificationList) {
                notificationService.deleteNotification(notification.getNotificationId());
            }
        }
        return "redirect:/userFront";
    }

    @RequestMapping(value = "/booking/rider", method = RequestMethod.GET)
    public String getRiderBookingHistory(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<BookingReference> bookingReferences = bookingService.findAll();
        bookingReferenceUtil.BookingReferenceStatusProcessor(AccountType.riderAccountType, bookingReferences);
        bookingReferences = bookingReferenceUtil.RiderBookingReferenceProcessor(user, bookingReferences);
        model.addAttribute("user", user);
        model.addAttribute("bookingReferences", bookingReferences);
        return "riderBookingHistory";
    }

    @RequestMapping(value = "/booking/driver", method = RequestMethod.GET)
    public String getDriverBookingHistory(Principal principal, Model model) {
        User user = userService.findByUsername(principal.getName());
        List<BookingReference> bookingReferences = bookingService.findAll();
        bookingReferenceUtil.BookingReferenceStatusProcessor(AccountType.driverAccountType, bookingReferences);
        bookingReferences = bookingReferenceUtil.DriverBookingReferenceProcessor(user, bookingReferences);
        model.addAttribute("user", user);
        model.addAttribute("bookingReferences", bookingReferences);
        return "driverBookingHistory";
    }

}
