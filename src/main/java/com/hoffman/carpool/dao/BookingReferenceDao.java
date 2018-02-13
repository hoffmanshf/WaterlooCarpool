package com.hoffman.carpool.dao;

import com.hoffman.carpool.domain.BookingReference;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BookingReferenceDao extends CrudRepository<BookingReference, Long> {
    List<BookingReference> findAll();
}
