package com.rebin.booking.reservation.presentation;

import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.reservation.dto.request.ReservationRequest;
import com.rebin.booking.reservation.dto.response.ReservationResponse;
import com.rebin.booking.reservation.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @PostMapping("")
    public ResponseEntity<ReservationResponse> reserve(@Auth Accessor accessor,
                                                       @RequestBody @Valid ReservationRequest request) {
        return ResponseEntity.ok(reservationService.reserve(accessor.getMemberId(), request));
    }
}
