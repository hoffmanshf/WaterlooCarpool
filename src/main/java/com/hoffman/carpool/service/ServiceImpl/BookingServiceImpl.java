package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.domain.constant.AccountType;
import com.hoffman.carpool.domain.constant.BookingReferenceStatus;
import com.hoffman.carpool.domain.entity.BookingReference;
import com.hoffman.carpool.domain.entity.DriverAccount;
import com.hoffman.carpool.domain.entity.RiderAccount;
import com.hoffman.carpool.domain.entity.User;
import com.hoffman.carpool.repository.BookingReferenceRepository;
import com.hoffman.carpool.repository.UserRepository;
import com.hoffman.carpool.service.BookingService;
import com.hoffman.carpool.util.DateTimeConverterUtil;
import com.hoffman.carpool.util.EmailNotificationUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {

    private static final String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sept", "Oct", "Nov", "Dec"};
    private static final String[] dayNames = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};

    private BookingReferenceRepository bookingReferenceRepository;

    private UserRepository userRepository;

    private DateTimeConverterUtil dateTimeConverterUtil;

    private EmailNotificationUtil emailNotificationUtil;

    @Autowired
    public BookingServiceImpl(BookingReferenceRepository bookingReferenceRepository, UserRepository userRepository, DateTimeConverterUtil dateTimeConverterUtil, EmailNotificationUtil emailNotificationUtil) {
        this.bookingReferenceRepository = bookingReferenceRepository;
        this.userRepository = userRepository;
        this.dateTimeConverterUtil = dateTimeConverterUtil;
        this.emailNotificationUtil = emailNotificationUtil;
    }

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
        if (accountType.equalsIgnoreCase(AccountType.driverAccountType)) {
            final DriverAccount driverAccount = user.getDriverAccount();
            bookingReference.setDriverAccount(driverAccount);
            final String author = driverAccount.getUsername();
            bookingReference.setAuthor(author);
        } else if (accountType.equalsIgnoreCase(AccountType.riderAccountType)) {
            final RiderAccount riderAccount = user.getRiderAccount();
            final int seatsReserved = bookingReference.getPassengerNumber();

            List<RiderAccount> passengerList = new ArrayList<>();
            for (int i = 0; i < seatsReserved; ++i) {
                passengerList.add(riderAccount);
            }
            bookingReference.setPassengerList(passengerList);
            final String author = riderAccount.getUsername();
            bookingReference.setAuthor(author);
        }

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
        final User author = userRepository.findByUsername(bookingReference.getAuthor());
        final String[] emails = {user.getEmail(), author.getEmail()};
        bookingReferenceRepository.save(bookingReference);
        emailNotificationUtil.sendNotification(emails, bookingReference);
    }

    @Override
    public void acceptRiderBooking(User user, BookingReference bookingReference) {

        final DriverAccount driverAccount = user.getDriverAccount();

        bookingReference.setDriverAccount(driverAccount);
        bookingReference.setBookingStatus(BookingReferenceStatus.IN_PROGRESS);
        final User author = userRepository.findByUsername(bookingReference.getAuthor());
        final String[] emails = {user.getEmail(), author.getEmail()};
        bookingReferenceRepository.save(bookingReference);
        emailNotificationUtil.sendNotification(emails, bookingReference);
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
    public List<BookingReference> findByAccountType(String accountType) {
        return bookingReferenceRepository.findByAccountType(accountType);
    }

    @Override
    public BookingReference saveBooking(BookingReference bookingReference) {
        return bookingReferenceRepository.save(bookingReference);
    }
}
