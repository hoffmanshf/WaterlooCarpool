package com.hoffman.carpool.util;

import com.hoffman.carpool.domain.BookingReference;
import com.hoffman.carpool.domain.PageWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Component
public class PaginationUtil {

    private static final int BUTTONS_TO_SHOW = 5;
    private static final int PAGE_SIZE = 5;
    private static final int INITIAL_PAGE = 0;

    public static PageWrapper PaginationProcessor(Model model, final Optional<Integer> page, List<BookingReference> bookingReferences) {
        final int evalPage = (page.orElse(0) < 1) ? INITIAL_PAGE : page.get() - 1;
        Pageable pageable = new PageRequest(evalPage, PAGE_SIZE);
        final int start = pageable.getOffset();
        final int end = (start + pageable.getPageSize()) > bookingReferences.size() ? bookingReferences.size() : (start + pageable.getPageSize());
        Page<BookingReference> pagedReferences = new PageImpl<BookingReference>(bookingReferences.subList(start, end), pageable, bookingReferences.size());
        PageWrapper wrapper = new PageWrapper(pagedReferences.getTotalPages(), pagedReferences.getNumber(), BUTTONS_TO_SHOW);
        model.addAttribute("bookingReferences", pagedReferences);
        return wrapper;
    }
}
