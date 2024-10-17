package com.rebin.booking.reservation.service.strategy;

import com.rebin.booking.common.excpetion.ReservationException;
import com.rebin.booking.reservation.dto.request.ReservationLookUpRequest;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.rebin.booking.common.excpetion.ErrorCode.NOT_SUPPORT_STATUS;

@Component
public class ReservationFinders {
    List<ReservationFinder> strategies;

    public ReservationFinders(List<ReservationFinder> strategies) {
        this.strategies = strategies;
    }

    public ReservationFinder mapping(final ReservationLookUpRequest statusRequest) {
        return strategies.stream().filter(
                strategy -> strategy.getStatus().equals(statusRequest)
        ).findFirst().orElseThrow(() -> new ReservationException(NOT_SUPPORT_STATUS));
    }
}
