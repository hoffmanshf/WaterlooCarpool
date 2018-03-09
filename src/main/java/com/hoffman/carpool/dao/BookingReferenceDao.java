package com.hoffman.carpool.dao;

import com.hoffman.carpool.domain.BookingReference;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingReferenceDao extends CrudRepository<BookingReference, Long> {
    List<BookingReference> findAll(Sort sort);
    List<BookingReference> findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContainingAndDateForSearch(String arrival, String departure, String date, Sort sort);
    List<BookingReference> findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContaining(String arrival, String departure, Sort sort);
}
