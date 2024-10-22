package com.rebin.booking.reservation.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;

@ExtendWith(MockitoExtension.class)
class ReservationCodeServiceTest {
    @InjectMocks
    ReservationCodeService reservationCodeService;
    @Spy
    CacheManager cacheManager = new ConcurrentMapCacheManager("reservationCodes");

    @Test
    void 캐시에_없는_코드는_유니크하다(){
        // given
        String code = "RE2410161043ABC";

        // when
        boolean isUnique = reservationCodeService.isCodeUnique(code);

        // then
        Assertions.assertTrue(isUnique);
    }

    @Test
    void 캐시에_있는_코드는_유니크하지않다(){
        // given
        String code ="RE2410161043ABC";
        reservationCodeService.isCodeUnique(code);

        // when
        boolean isUnique = reservationCodeService.isCodeUnique(code);

        // then
        Assertions.assertFalse(isUnique);
    }
}