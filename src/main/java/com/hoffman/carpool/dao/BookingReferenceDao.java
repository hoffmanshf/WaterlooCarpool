package com.hoffman.carpool.dao;

import com.hoffman.carpool.domain.BookingReference;
import org.springframework.data.repository.CrudRepository;

import java.util.Date;
import java.util.List;

public interface BookingReferenceDao extends CrudRepository<BookingReference, Long> {
    List<BookingReference> findAll();
    List<BookingReference> findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContainingAndDateForSearch(String arrival, String departure, String date);
}
