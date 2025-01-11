package com.rebin.booking.reservation.presentation;

import com.rebin.booking.reservation.dto.response.AdminReservationResponse;
import com.rebin.booking.reservation.dto.response.ReservationCountResponse;
import com.rebin.booking.reservation.dto.response.ReservationDailyResponse;
import com.rebin.booking.reservation.dto.response.ReservationDetailResponse;
import com.rebin.booking.reservation.service.AdminReservationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/admin/reservations")
@RequiredArgsConstructor
public class AdminReservationController {
    private final AdminReservationService adminReservationService;

    @Operation(summary = "당월 예약 건수 조회")
    @GetMapping("/month/count")
    public ResponseEntity<ReservationCountResponse> countReservationThisMonth() {
        return ResponseEntity.ok(adminReservationService.countReservationsThisMonth());
    }

    @Operation(summary = "이번 주 예약 조회")
    @GetMapping("/month")
    public ResponseEntity<List<ReservationDailyResponse>> getReservationsThisMonth(){
        return ResponseEntity.ok(adminReservationService.getReservationsByWeek());
    }

    @Operation(summary = "예약 조회 with parameter")
    @GetMapping
    public ResponseEntity<List<AdminReservationResponse>> getReservationWithParam(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate,
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String name
    ) {
        List<AdminReservationResponse> reservations = adminReservationService.getReservationsWithParam(startDate, endDate, code, name);
        return ResponseEntity.ok(reservations);
    }

    @Operation(summary = "예약 상세조회 with code")
    @GetMapping("/{code}")
    public ResponseEntity<ReservationDetailResponse> getReservation(
            @PathVariable String code
    ) {
        return ResponseEntity.ok(adminReservationService.getReservationByCode(code));
    }

    @Operation(summary = "입금 확인 요청 accept")
    @PatchMapping("/deposit/{reservationId}")
    public ResponseEntity<Void> confirmPaymentRequest(@PathVariable Long reservationId) {
        adminReservationService.confirmPaymentRequest(reservationId);
        return ResponseEntity.noContent().build();
    }
}
