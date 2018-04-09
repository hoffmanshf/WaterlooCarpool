package com.hoffman.carpool.repository;

import com.hoffman.carpool.domain.entity.BookingReference;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingReferenceRepository extends CrudRepository<BookingReference, Long> {
    List<BookingReference> findAll();
    List<BookingReference> findByAccountType(String accountType);
    List<BookingReference> findByAccountType(String accountType, Sort sort);
    List<BookingReference> findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContainingAndDateForSearchAndAccountType(String arrival, String departure, String date, String accountType, Sort sort);
    List<BookingReference> findByArrivalIgnoreCaseContainingAndDepartureIgnoreCaseContainingAndAccountType(String arrival, String departure, String accountType, Sort sort);
}
