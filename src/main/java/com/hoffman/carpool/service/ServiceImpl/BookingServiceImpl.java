package com.hoffman.carpool.service.ServiceImpl;

import com.hoffman.carpool.dao.BookingReferenceDao;
import com.hoffman.carpool.domain.BookingReference;
import com.hoffman.carpool.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingReferenceDao bookingReferenceDao;

    @Override
    public BookingReference createRiderBooking(BookingReference bookingReference) {
        return bookingReferenceDao.save(bookingReference);
    }

    @Override
    public BookingReference findBookingReference(Long id) {
        return bookingReferenceDao.findOne(id);
    }

    @Override
    public List<BookingReference> findAll() {
        return bookingReferenceDao.findAll();
    }
}
