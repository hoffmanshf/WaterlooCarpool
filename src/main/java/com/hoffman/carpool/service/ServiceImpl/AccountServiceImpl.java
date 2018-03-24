package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.domain.*;
import com.hoffman.carpool.repository.BookingReferenceRepository;
import com.hoffman.carpool.repository.CarRepository;
import com.hoffman.carpool.repository.DriverAccountRepository;
import com.hoffman.carpool.repository.RiderAccountRepository;
import com.hoffman.carpool.service.AccountService;
import com.hoffman.carpool.util.BookingReferenceUtil;
import com.hoffman.carpool.util.SortingUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private DriverAccountRepository driverAccountRepository;

    @Autowired
    private RiderAccountRepository riderAccountRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingReferenceRepository bookingReferenceRepository;

    @Autowired
    private BookingReferenceUtil bookingReferenceUtil;

    @Autowired
    private SortingUtil sortingUtil;

    @Override
    public DriverAccount createDriverAccount(final User user) {
        final DriverAccount driverAccount = new DriverAccount();
        final Car car = new Car();
        driverAccount.setEmail(user.getEmail());
        driverAccount.setPhone(user.getPhone());
        driverAccount.setUsername(user.getUsername());
        driverAccount.setFirstName(user.getFirstName());
        driverAccount.setLastName(user.getLastName());
        driverAccount.setCar(carRepository.save(car));
        driverAccountRepository.save(driverAccount);
        return driverAccountRepository.findByDriverAccountId(driverAccount.getDriverAccountId());
    }

    @Override
    public RiderAccount createRiderAccount(final User user) {
        final RiderAccount riderAccount = new RiderAccount();
        riderAccount.setEmail(user.getEmail());
        riderAccount.setPhone(user.getPhone());
        riderAccount.setUsername(user.getUsername());
        riderAccount.setFirstName(user.getFirstName());
        riderAccount.setLastName(user.getLastName());
        riderAccountRepository.save(riderAccount);
        return riderAccountRepository.findByRiderAccountId(riderAccount.getRiderAccountId());
    }

    @Override
    public List<BookingReference> getAccountBookingReference(final String sort, final String accountType, final User user) {
        final Sort sortType = sortingUtil.getSortType(sort);
        List<BookingReference> bookingReferences = bookingReferenceRepository.findAll(sortType);
        bookingReferenceUtil.BookingReferenceStatusProcessor(accountType, bookingReferences);
        bookingReferences = bookingReferenceUtil.BookingReferenceProcessor(accountType, user, bookingReferences);
        return bookingReferences;
    }

    @Override
    public List<BookingReference> getAccountSearchResult(final String sort, final String accountType, final String date,
                                                         final String arrival, final String departure, final User user, final String... passengerNumber) {

        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
        final String arrivalCity = bookingReferenceUtil.AddressCityProcessor(arrival);
        final String departureCity = bookingReferenceUtil.AddressCityProcessor(departure);

        final Sort sortType = sortingUtil.getSortType(sort);

        if (date != null && StringUtils.isNotEmpty(date)) {
            bookingReferences = bookingReferenceRepository.findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContainingAndDateForSearch(arrivalCity, departureCity, date, sortType);
        } else {
            bookingReferences = bookingReferenceRepository.findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContaining(arrivalCity, departureCity, sortType);
        }

        bookingReferenceUtil.BookingReferenceStatusProcessor(accountType, bookingReferences);
        bookingReferences = bookingReferenceUtil.BookingReferenceProcessor(accountType, user, bookingReferences);

        if (passengerNumber.length > 0) {
            if (passengerNumber != null && StringUtils.isNotEmpty(passengerNumber[0])) {
                final int seats = Integer.valueOf(passengerNumber[0]);
                List<BookingReference> filteredBookingReferences = new ArrayList<BookingReference>();
                for (final BookingReference reference : bookingReferences) {
                    if (seats <= reference.getPassengerNumber()) {
                        filteredBookingReferences.add(reference);
                    }
                }
                bookingReferences = filteredBookingReferences;
            }
        }

        return bookingReferences;
    }
}
