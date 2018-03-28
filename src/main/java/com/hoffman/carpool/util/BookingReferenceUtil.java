package com.hoffman.carpool.util;

import com.hoffman.carpool.domain.constant.AccountType;
import com.hoffman.carpool.domain.constant.BookingReferenceStatus;
import com.hoffman.carpool.domain.entity.BookingReference;
import com.hoffman.carpool.domain.entity.RiderAccount;
import com.hoffman.carpool.domain.entity.User;
import com.hoffman.carpool.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Component
public class BookingReferenceUtil {

    @Autowired
    private BookingService bookingService;

    public static List<BookingReference> BookingReferenceProcessor(final String accountType, final User user, List<BookingReference> UserBookingReferences) {
        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
        for (final BookingReference reference: UserBookingReferences) {
            if (reference.getAccountType().equalsIgnoreCase(accountType)) {
                if (reference.getAuthor().equalsIgnoreCase(user.getUsername())) {
                    reference.setOwner(true);
                }
                if (!reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.CANCELLED) &&
                        !reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.COMPLETE) &&
                        !reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.EXPIRED) && reference.getPassengerNumber() != 0) {
                    if (reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS) &&
                            reference.getAccountType().equalsIgnoreCase(AccountType.riderAccountType)) {
                        continue;
                    }
                    bookingReferences.add(reference);
                }
            }
        }
        return bookingReferences;
    }

    public static List<BookingReference> RiderBookingReferenceProcessor(final User user, List<BookingReference> UserBookingReferences) {
        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
        for (final BookingReference reference: UserBookingReferences) {
            if (reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.BACK_PENDING)) {
                reference.setBookingStatus(BookingReferenceStatus.CANCELLED);
            }
            List<RiderAccount> accounts = reference.getPassengerList();
            List<RiderAccount> cancelledAccounts = reference.getCancelledPassengerList();
            if (accounts != null) {
                for (RiderAccount account : accounts) {
                    if (account.getUsername().equalsIgnoreCase(user.getUsername()) && !cancelledAccounts.contains(account)) {
                        bookingReferences.add(reference);
                        break;
                    }
                }
            }

            if (cancelledAccounts != null) {
                for (RiderAccount cancelledAccount : cancelledAccounts) {
                    if (cancelledAccount.getUsername().equalsIgnoreCase(user.getUsername())) {
                        bookingReferences.add(reference);
                        break;
                    }
                }
            }
        }
        return bookingReferences;
    }

    public static List<BookingReference> DriverBookingReferenceProcessor(final User user, List<BookingReference> UserBookingReferences) {
        List<BookingReference> bookingReferences = new ArrayList<BookingReference>();
        for (final BookingReference reference: UserBookingReferences) {
            if (reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.BACK_PENDING)) {
                reference.setBookingStatus(BookingReferenceStatus.IN_PROGRESS);
            }
            if (reference.getDriverAccount() != null) {
                if (reference.getDriverAccount().getUsername().equalsIgnoreCase(user.getUsername())) {
                    bookingReferences.add(reference);
                    continue;
                }
            }
        }
        return bookingReferences;
    }

    public void BookingReferenceStatusProcessor(final String accountType, List<BookingReference> UserBookingReferences) {
        Date today = new Date();
        for (final BookingReference reference: UserBookingReferences) {
            if (reference.getAccountType().equalsIgnoreCase(accountType)) {
                if (reference.getDate().before(today)) {
                    if (reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.PENDING)) {
                        reference.setBookingStatus(BookingReferenceStatus.EXPIRED);
                        bookingService.saveBooking(reference);
                    } else if (reference.getBookingStatus().equalsIgnoreCase(BookingReferenceStatus.IN_PROGRESS)) {
                        reference.setBookingStatus(BookingReferenceStatus.COMPLETE);
                        bookingService.saveBooking(reference);
                    }
                }
            }
        }
    }

    public static String AddressCityProcessor(final String address) {
        final String finalAddress;
        if (address.contains(",")) {
            finalAddress = address.substring(0, address.indexOf(","));
        } else {
            finalAddress = address;
        }
        return finalAddress;
    }
}
