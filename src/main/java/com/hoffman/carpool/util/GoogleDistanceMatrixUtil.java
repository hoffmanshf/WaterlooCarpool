package com.hoffman.carpool.util;

import com.google.maps.DirectionsApi;
import com.google.maps.DistanceMatrixApi;
import com.google.maps.DistanceMatrixApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DistanceMatrix;
import com.google.maps.model.TravelMode;
import com.hoffman.carpool.domain.entity.BookingReference;
import com.hoffman.carpool.error.UServiceException;
import com.hoffman.carpool.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static java.lang.Math.toIntExact;

@Component
public class GoogleDistanceMatrixUtil {

    private static final String API_KEY = "AIzaSyDy3AhiOW3Z4C3GZptz5bMLWoXsL6KjyAM";
    private static final GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();

    private BookingService bookingService;

    private DateTimeConverterUtil dateTimeConverterUtil;

    @Autowired
    public GoogleDistanceMatrixUtil(BookingService bookingService, DateTimeConverterUtil dateTimeConverterUtil) {
        this.bookingService = bookingService;
        this.dateTimeConverterUtil = dateTimeConverterUtil;
    }

    @Async("googleServiceExecutor")
    public void estimateRouteTime(String departure, String arrival, String source, BookingReference bookingReference) {
        Date date = dateTimeConverterUtil.StringToDateConverter(source);
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        try {
            DistanceMatrixApiRequest req = DistanceMatrixApi.newRequest(context);

            DistanceMatrix distanceMatrix = req.origins(departure)
                    .destinations(arrival)
                    .mode(TravelMode.DRIVING)
                    .avoid(DirectionsApi.RouteRestriction.TOLLS)
                    .language("en-CA")
                    .await();
            String distance = distanceMatrix.rows[0].elements[0].distance.humanReadable;
            String duration = distanceMatrix.rows[0].elements[0].duration.humanReadable;
            long durationData = distanceMatrix.rows[0].elements[0].duration.inSeconds;
            int actualDuration = toIntExact(durationData);
            calendar.add(Calendar.SECOND, actualDuration);
            bookingReference.setArrivalTime(calendar);
            bookingReference.setDistance(distance);
            bookingReference.setDuration(duration);
            bookingService.saveBooking(bookingReference);
        } catch (ApiException e) {
            throw new UServiceException("TXN_103","", "Api parse error", e);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
