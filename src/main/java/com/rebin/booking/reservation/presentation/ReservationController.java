package com.rebin.booking.reservation.presentation;

import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.auth.domain.MemberOnly;
import com.rebin.booking.reservation.dto.request.ReservationRequest;
import com.rebin.booking.reservation.dto.request.ReservationLookUpRequest;
import com.rebin.booking.reservation.dto.response.ReservationDetailResponse;
import com.rebin.booking.reservation.dto.response.ReservationResponse;
import com.rebin.booking.reservation.dto.response.ReservationSaveResponse;
import com.rebin.booking.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("")
    @MemberOnly
    public ResponseEntity<ReservationSaveResponse> reserve(@Auth Accessor accessor,
                                                           @RequestBody @Valid ReservationRequest request) {
        return ResponseEntity.ok(reservationService.reserve(accessor.getMemberId(), request));
    }

    @GetMapping("")
    @MemberOnly
    public ResponseEntity<List<ReservationResponse>> getReservationsByStatus(@Auth Accessor accessor,
                                                                             @RequestParam(value = "status") ReservationLookUpRequest request) {
        return ResponseEntity.ok(reservationService.getReservationsByStatus(accessor.getMemberId(), request));
    }

    @GetMapping("/{reservationId}")
    @MemberOnly
    public ResponseEntity<ReservationDetailResponse> getReservation(@Auth Accessor accessor,
                                                                    @PathVariable(value = "reservationId") Long reservationId) {
        return ResponseEntity.ok(reservationService.getReservationDetail(accessor.getMemberId(), reservationId));
    }

    @PatchMapping("/{reservationId}")
    @MemberOnly
    public ResponseEntity<Void> cancelReservation(@Auth Accessor accessor,
                                                  @PathVariable(value = "reservationId") Long reservationId) {
        reservationService.cancelReservation(accessor.getMemberId(), reservationId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{reservationId}/payment")
    @MemberOnly
    public ResponseEntity<Void> requestPaymentConfirmation(@Auth Accessor accessor,
                                              @PathVariable(value = "reservationId") Long reservationId) {
        reservationService.requestPaymentConfirmation(accessor.getMemberId(), reservationId);
        return ResponseEntity.noContent().build();
    }

}
