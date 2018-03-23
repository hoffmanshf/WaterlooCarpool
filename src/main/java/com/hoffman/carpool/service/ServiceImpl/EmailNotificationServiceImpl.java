package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.domain.CalendarEvent;
import com.hoffman.carpool.error.UServiceException;
import com.hoffman.carpool.service.EmailNotificationService;
import com.hoffman.carpool.util.CalendarEventUtil;
import net.fortuna.ical4j.data.CalendarOutputter;
import net.fortuna.ical4j.model.Calendar;
import net.fortuna.ical4j.model.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.mail.util.ByteArrayDataSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {

    @Autowired
    private JavaMailSender sender;

    @Override
    @Async("emailNotificationExecutor")
    public void sendNotification (String toAddress, final CalendarEvent calendarEvent) throws MailException {

        Calendar calendar = CalendarEventUtil.createEventCalendar(calendarEvent);
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
