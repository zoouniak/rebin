package com.rebin.booking.reservation.service;

import com.rebin.booking.common.excpetion.ErrorCode;
import com.rebin.booking.common.excpetion.ReservationException;
import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.repository.CustomReservationRepository;
import com.rebin.booking.reservation.domain.repository.ReservationRepository;
import com.rebin.booking.reservation.dto.response.AdminReservationResponse;
import com.rebin.booking.reservation.dto.response.ReservationCountResponse;
import com.rebin.booking.reservation.dto.response.ReservationDetailResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomReservationRepository customReservationRepository;

    public ReservationCountResponse countReservationsThisMonth() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        Month month = Month.FEBRUARY;
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = LocalDate.of(year, month, month.maxLength());

        int count = reservationRepository.countByShootDateBetween(startDate, endDate);
        return new ReservationCountResponse(count);
    }

    public List<AdminReservationResponse> getReservationsWithParam(final LocalDate startDate, final LocalDate endDate,
                                                                   final String code, final String name) {
        return customReservationRepository.getReservations(startDate, endDate, code, name);
    }

    public ReservationDetailResponse getReservationByCode(final String code) {
        Reservation reservation = reservationRepository.findByCode(code)
                .orElseThrow(() -> new ReservationException(ErrorCode.INVALID_RESERVATION));

        return ReservationDetailResponse.of(reservation);
    }
}
