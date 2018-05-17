package com.hoffman.carpool.util;

import com.hoffman.carpool.domain.CalendarEvent;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.Date;
import net.fortuna.ical4j.model.DateTime;
import net.fortuna.ical4j.model.component.VEvent;
import net.fortuna.ical4j.model.property.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import java.net.URI;

@Component
public class CalendarEventUtil {

    public static Calendar createEventCalendar(final CalendarEvent calendarEvent) {
        Calendar calendar = new Calendar();
        calendar.getProperties().add(new ProdId("-//Hippo Event Calendar//iCal4j 1.0//EN"));
        calendar.getProperties().add(Version.VERSION_2_0);
        calendar.getProperties().add(CalScale.GREGORIAN);

        VEvent vEvent = createVEvent(calendarEvent);

        calendar.getComponents().add(vEvent);
        return calendar;
    }

    private static VEvent createVEvent(final CalendarEvent calendarEvent) {
        Date start = new DateTime(calendarEvent.getStartDate().getTime());
        Date end = new DateTime(calendarEvent.getEndDate().getTime());

        VEvent vEvent = new VEvent(start, end, calendarEvent.getTitle());
        Uid uid = new Uid(calendarEvent.getCanonicalHandleUUID());
        vEvent.getProperties().add(uid);

        if (StringUtils.isNotEmpty(calendarEvent.getSummary())) {
            vEvent.getProperties().add(new Description(calendarEvent.getSummary()));
        }

        return vEvent;
    }


}
