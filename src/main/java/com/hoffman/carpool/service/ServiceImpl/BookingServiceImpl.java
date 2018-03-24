package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.repository.BookingReferenceRepository;
import com.hoffman.carpool.domain.BookingReference;
import com.hoffman.carpool.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingReferenceRepository bookingReferenceRepository;

    @Override
    public BookingReference createBooking(BookingReference bookingReference) {
        return bookingReferenceRepository.save(bookingReference);
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
    public List<BookingReference> findAll(Sort sort) {
        return bookingReferenceRepository.findAll(sort);
    }

    @Override
    public BookingReference saveBooking(BookingReference bookingReference) {
        return bookingReferenceRepository.save(bookingReference);
    }

    @Override
    public List<BookingReference> searchBookingReference(String arrival, String departure, String date, Sort sort) {
        return bookingReferenceRepository.findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContainingAndDateForSearch(arrival, departure, date, sort);
    }

    @Override
    public List<BookingReference> searchBookingReferenceWithoutDate(String arrival, String departure, Sort sort) {
        return bookingReferenceRepository.findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContaining(arrival, departure, sort);
    }

}
