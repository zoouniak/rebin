package com.rebin.booking.reservation.service.strategy;

import com.rebin.booking.reservation.dto.request.ReservationLookUpRequest;
import com.rebin.booking.reservation.dto.response.ReservationResponse;

import java.util.List;

public interface ReservationFinder {
    List<ReservationResponse> getReservations(Long memberId);
    ReservationLookUpRequest getStatus();
}
