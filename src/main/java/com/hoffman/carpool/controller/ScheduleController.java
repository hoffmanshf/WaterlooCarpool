package com.hoffman.carpool.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/schedule")
public class ScheduleController {

    @RequestMapping("/drive")
    public String driverSchedule() {
        return "driverSchedule";
    }


}
