package com.hoffman.carpool.util;

import com.hoffman.carpool.error.UServiceException;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class DateTimeConverterUtil {

    public static Date StringToDateConverter(final String source) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = null;

        try {
            if (!source.isEmpty() && source != null) {
                date = format.parse(source);
            }
        } catch (ParseException e) {
            throw new UServiceException("TXN_101","", "Date parse error", e);
        }
        return date;
    }

    public static String DateToTimeConverter(final Date date) {

        String time = null;
        if (date != null) {
            time = DateFormat.getTimeInstance(DateFormat.SHORT).format(date);
        }
        return time;
    }

}
