package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.Car;
import com.hoffman.carpool.domain.User;
import com.hoffman.carpool.service.CarService;
import com.hoffman.carpool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private CarService carService;

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
}
