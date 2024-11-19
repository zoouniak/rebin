package com.rebin.booking.reservation.util;

import org.springframework.stereotype.Component;

@Component
public class PriceCalculator {
    private static final int DEFAULT_PEOPLE = 1;

    public int calculatePrice(int productPrice, int peopleCnt, int additionalFee) {
        return ((peopleCnt - DEFAULT_PEOPLE) * additionalFee) + productPrice;
    }
}
