package com.rebin.booking.reservation.validator;

import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.type.ReservationStatusType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationCancelValidatorTest {
    @InjectMocks
    ReservationCancelValidator validator;
    @Mock
    Reservation reservation;

    // 취소 가능
    @Test
    void 촬영_전이고_취소가능기간인_예약() {
        when(reservation.getStatus()).thenReturn(ReservationStatusType.PAYMENT_CONFIRMED);
        when(reservation.getShootDate()).thenReturn(LocalDate.now().plusDays(10));

        boolean result = validator.canCancelReservation(reservation);

        Assertions.assertTrue(result);
    }

    // 취소 불가
    @Test
    void 이미_취소된_예약() {
        when(reservation.getStatus()).thenReturn(ReservationStatusType.CANCELED);
        // when(reservation.getShootDate()).thenReturn(LocalDate.now().plusDays(10));

        boolean result = validator.canCancelReservation(reservation);

        Assertions.assertFalse(result);
    }

    @Test
    void 촬영완료된_예약() {
        when(reservation.getStatus()).thenReturn(ReservationStatusType.COMPLETED);
        // when(reservation.getShootDate()).thenReturn(LocalDate.now().plusDays(10));

        boolean result = validator.canCancelReservation(reservation);

        Assertions.assertFalse(result);
    }

    @Test
    void 취소가능기간이_지난_예약() {
        when(reservation.getStatus()).thenReturn(ReservationStatusType.PENDING_PAYMENT);
        when(reservation.getShootDate()).thenReturn(LocalDate.now().plusDays(5));

        boolean result = validator.canCancelReservation(reservation);

        Assertions.assertFalse(result);
    }
}