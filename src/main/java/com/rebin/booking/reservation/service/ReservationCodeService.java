package com.rebin.booking.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservationCodeService {
    private final CacheManager cacheManager;
    private final static String CACHE = "reservationCodes";
    public boolean isCodeUnique(String code) {
        Cache cache = cacheManager.getCache(CACHE);
        Cache.ValueWrapper valueWrapper = cache.get(code);
        if (valueWrapper != null) {
            return false;
        } else {
            cache.put(code, true);
            return true;
        }
    }

    @Scheduled(cron = "0 * * * * *")
    public void clearCache() {
        Cache cache = cacheManager.getCache(CACHE);
        if (cache != null) {
            cache.clear();
            log.info("Reservation-Code Cache REMOVED");
        }
    }
}
