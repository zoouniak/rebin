package com.rebin.booking.reservation.service;

import com.rebin.booking.common.excpetion.ErrorCode;
import com.rebin.booking.common.excpetion.ReservationException;
import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.repository.CustomReservationRepository;
import com.rebin.booking.reservation.domain.repository.ReservationRepository;
import com.rebin.booking.reservation.dto.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminReservationService {
    private final ReservationRepository reservationRepository;
    private final CustomReservationRepository customReservationRepository;

    public ReservationCountResponse countReservationsThisMonth() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();
        Month month = now.getMonth();

        YearMonth yearMonth = YearMonth.of(year, month.getValue());
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

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

    @Transactional
    public void confirmPaymentRequest(final Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationException(ErrorCode.INVALID_RESERVATION));

        reservation.confirm();
    }

    public List<ReservationDailyResponse> getReservationsByWeek() {
        return customReservationRepository.getReservationsThisMonth();
    }
}
