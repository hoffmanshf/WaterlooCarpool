package com.hoffman.carpool.domain.constant;

import org.springframework.data.domain.Sort;

public class SortingType {
    public static final Sort sortByDateASC = new Sort(Sort.Direction.ASC, "date");
    public static final Sort sortByPriceASC = new Sort(Sort.Direction.ASC, "price");
    public static final Sort sortByDateDESC = new Sort(Sort.Direction.DESC, "date");
    public static final Sort sortByPriceDESC = new Sort(Sort.Direction.DESC, "price");
}
