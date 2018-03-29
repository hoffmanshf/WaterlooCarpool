package com.hoffman.carpool.util;

import com.hoffman.carpool.domain.entity.BookingReference;
import com.hoffman.carpool.domain.CalendarEvent;
import com.hoffman.carpool.error.UServiceException;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;

@Component
public class EmailNotificationUtil {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private CalendarEventUtil calendarEventUtil;

    @Async("emailNotificationExecutor")
    public void sendNotification (final String[] toAddress, final BookingReference bookingReference) throws MailException {

        GregorianCalendar departureCalendar = new GregorianCalendar();
        departureCalendar.setTime(bookingReference.getDate());

        StringBuilder summary = new StringBuilder();
        summary.append("From ");
        summary.append(bookingReference.getDeparture());
        summary.append(" To ");
        summary.append(bookingReference.getArrival());

        CalendarEvent calendarEvent = new CalendarEvent("Waterloo Carpool", departureCalendar, bookingReference.getArrivalTime(), summary.toString(), "notification", "1");


        Calendar calendar = calendarEventUtil.createEventCalendar(calendarEvent);
        byte[] attachmentData = calendarAsByteArray(calendar);

        MimeMessage mimeMessage = sender.createMimeMessage();

        try {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setTo(toAddress);
            mimeMessageHelper.setSubject("Your booking has been accepted");
            mimeMessageHelper.setText("Thank you for choosing Waterloo Carpool.\n\nYour trip schedule is attached.\n");
            StringBuilder builder = new StringBuilder();
            builder.append(calendarEvent.getName());
            builder.append(".ics");
            String contentType = String.format("text/calendar; name=%s", builder.toString());
            mimeMessageHelper.addAttachment("calendar",new ByteArrayDataSource(attachmentData, contentType));
            sender.send(mimeMessage);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private byte[] calendarAsByteArray(final Calendar iCalendar) {
        byte[] bytes;
        try {
            ByteArrayOutputStream output;
            output = new ByteArrayOutputStream();
            CalendarOutputter calendarOutputter = new CalendarOutputter();
            calendarOutputter.output(iCalendar, output);
            bytes = output.toByteArray();
        } catch (ValidationException e) {
            throw new UServiceException("TXN_106","", "Calendar validation error", e);
        } catch (IOException e) {
            throw new UServiceException("TXN_105","", "Calendar parse error", e);
        }
        return bytes;
    }
}
