package com.hoffman.carpool.controller;

import com.hoffman.carpool.domain.BookingReference;
import com.hoffman.carpool.domain.DriverAccount;
import com.hoffman.carpool.domain.RiderAccount;
import com.hoffman.carpool.domain.User;
import com.hoffman.carpool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;

@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    private UserService userService;

    @RequestMapping("/riderAccount")
    public String riderAccount(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        RiderAccount riderAccount = user.getRiderAccount();
        model.addAttribute("riderAccount", riderAccount);
        return "riderAccount";
    }

    @RequestMapping("/driverAccount")
    public String driverAccount(Model model, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        DriverAccount driverAccount = user.getDriverAccount();
        model.addAttribute("driverAccount", driverAccount);
        return "driverAccount";
    }

}
