package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.domain.entity.*;
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

    private DriverAccountRepository driverAccountRepository;

    private RiderAccountRepository riderAccountRepository;

    private CarRepository carRepository;

    private BookingReferenceRepository bookingReferenceRepository;

    private BookingReferenceUtil bookingReferenceUtil;

    private SortingUtil sortingUtil;

    @Autowired
    public AccountServiceImpl(DriverAccountRepository driverAccountRepository, RiderAccountRepository riderAccountRepository, CarRepository carRepository, BookingReferenceRepository bookingReferenceRepository, BookingReferenceUtil bookingReferenceUtil, SortingUtil sortingUtil) {
        this.driverAccountRepository = driverAccountRepository;
        this.riderAccountRepository = riderAccountRepository;
        this.carRepository = carRepository;
        this.bookingReferenceRepository = bookingReferenceRepository;
        this.bookingReferenceUtil = bookingReferenceUtil;
        this.sortingUtil = sortingUtil;
    }

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
        List<BookingReference> bookingReferences = bookingReferenceRepository.findByAccountType(accountType, sortType);
        bookingReferenceUtil.BookingReferenceStatusProcessor(bookingReferences);
        bookingReferences = bookingReferenceUtil.BookingReferenceProcessor(user, bookingReferences);
        return bookingReferences;
    }

    @Override
    public List<BookingReference> getAccountSearchResult(final String sort, final String accountType, final String date,
                                                         final String arrival, final String departure, final User user, final String... passengerNumber) {

        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
        final String arrivalCity = bookingReferenceUtil.AddressCityProcessor(arrival);
        final String departureCity = bookingReferenceUtil.AddressCityProcessor(departure);

        final Sort sortType = sortingUtil.getSortType(sort);

        if (StringUtils.isNotEmpty(date)) {
            bookingReferences = bookingReferenceRepository.findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContainingAndDateForSearchAndAccountType(arrivalCity, departureCity, date, accountType, sortType);
        } else {
            bookingReferences = bookingReferenceRepository.findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContainingAndAccountType(arrivalCity, departureCity, accountType, sortType);
        }

        bookingReferenceUtil.BookingReferenceStatusProcessor(bookingReferences);
        bookingReferences = bookingReferenceUtil.BookingReferenceProcessor(user, bookingReferences);

        if (passengerNumber.length > 0) {
            if (StringUtils.isNotEmpty(passengerNumber[0])) {
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
