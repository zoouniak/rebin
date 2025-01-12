package com.rebin.booking.reservation.domain.repository;

import com.rebin.booking.reservation.dto.response.AdminReservationResponse;
import com.rebin.booking.reservation.dto.response.ReservationDailyResponse;

import java.time.LocalDate;
import java.util.List;

public interface CustomReservationRepository {
    List<AdminReservationResponse> getReservations(LocalDate startDate, LocalDate endDate, String code, String name);

    List<ReservationDailyResponse> getReservationsThisMonth();
}
