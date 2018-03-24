package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.domain.*;
import com.hoffman.carpool.repository.BookingReferenceRepository;
import com.hoffman.carpool.repository.UserRepository;
import com.hoffman.carpool.service.BookingService;
import com.hoffman.carpool.util.DateTimeConverterUtil;
import com.hoffman.carpool.util.EmailNotificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {

    private static final String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
    private static final String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    @Autowired
    BookingReferenceRepository bookingReferenceRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DateTimeConverterUtil dateTimeConverterUtil;

    @Autowired
    EmailNotificationUtil emailNotificationUtil;

    @Override
    public void createBooking(final BookingReference bookingReference, final String source, final String accountType, final Principal principal) {

        final Date date = dateTimeConverterUtil.StringToDateConverter(source);
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);

        final String month = monthNames[calendar.get(Calendar.MONTH)];
        final String dayOfWeek = dayNames[calendar.get(Calendar.DAY_OF_WEEK) - 1];
        final String dayOfMonth = new Integer(calendar.get(Calendar.DAY_OF_MONTH)).toString();

        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        final String dateForSearch = formatter.format(date);

        final String time = dateTimeConverterUtil.DateToTimeConverter(date);

        bookingReference.setDayOfMonth(dayOfMonth);
        bookingReference.setDayOfWeek(dayOfWeek);
        bookingReference.setMonth(month);
        bookingReference.setDate(date);
        bookingReference.setTime(time);
        bookingReference.setDateForSearch(dateForSearch);
        bookingReference.setAccountType(accountType);

        final User user = userRepository.findByUsername(principal.getName());
        final DriverAccount driverAccount = user.getDriverAccount();
        bookingReference.setDriverAccount(driverAccount);

        final String author = driverAccount.getUsername();
        bookingReference.setAuthor(author);
        bookingReference.setBookingStatus(BookingReferenceStatus.PENDING);

        bookingReferenceRepository.save(bookingReference);
    }

    @Override
    public void acceptDriverBooking(final User user, final Integer seatsReserved, final BookingReference bookingReference) {

        final RiderAccount riderAccount = user.getRiderAccount();

        int availableSeats = bookingReference.getPassengerNumber();
        availableSeats -= seatsReserved;
        bookingReference.setPassengerNumber(availableSeats);

        List<RiderAccount> passengerList = bookingReference.getPassengerList();
        for (int i = 0; i < seatsReserved; ++i) {
            passengerList.add(riderAccount);
        }
        bookingReference.setPassengerList(passengerList);
        bookingReference.setBookingStatus(BookingReferenceStatus.IN_PROGRESS);

        bookingReferenceRepository.save(bookingReference);
        emailNotificationUtil.sendNotification(user.getEmail(), bookingReference);
    }

    @Override
    public BookingReference findBookingReference(Long id) {
        return bookingReferenceRepository.findOne(id);
    }

    @Override
    public List<BookingReference> findAll() {
        return bookingReferenceRepository.findAll();
    }

    @Override
    public BookingReference saveBooking(BookingReference bookingReference) {
        return bookingReferenceRepository.save(bookingReference);
    }
}
