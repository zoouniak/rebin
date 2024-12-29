package com.rebin.booking.reservation.presentation;

import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.auth.domain.MemberOnly;
import com.rebin.booking.reservation.dto.request.ConfirmRequest;
import com.rebin.booking.reservation.dto.request.ReservationRequest;
import com.rebin.booking.reservation.dto.request.ReservationLookUpRequest;
import com.rebin.booking.reservation.dto.request.ReservationUpdateRequest;
import com.rebin.booking.reservation.dto.response.MemberInfoResponse;
import com.rebin.booking.reservation.dto.response.ReservationDetailResponse;
import com.rebin.booking.reservation.dto.response.ReservationResponse;
import com.rebin.booking.reservation.dto.response.ReservationSaveResponse;
import com.rebin.booking.reservation.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @Operation(summary = "예약하기")
    @PostMapping("")
    @MemberOnly
    public ResponseEntity<ReservationSaveResponse> reserve(@Auth Accessor accessor,
                                                           @RequestBody @Valid ReservationRequest request) {
        return ResponseEntity.ok(reservationService.reserve(accessor.getMemberId(), request));
    }

    @Operation(summary = "예약자 자동 완성을 위한 사용자 정보")
    @GetMapping("/auto-fill")
    @MemberOnly
    public ResponseEntity<MemberInfoResponse> getAutoFillMemberInfo(@Auth Accessor accessor){
        return ResponseEntity.ok(reservationService.getMemberInfoForAutoFill(accessor.getMemberId()));
    }

    @Operation(summary = "예약 상태 별 조회(촬영 전/중/후)")
    @GetMapping("")
    @MemberOnly
    public ResponseEntity<List<ReservationResponse>> getReservationsByStatus(@Auth Accessor accessor,
                                                                             @RequestParam(value = "status") ReservationLookUpRequest request) {
        return ResponseEntity.ok(reservationService.getReservationsByStatus(accessor.getMemberId(), request));
    }

    @Operation(summary = "예약 상세 조회")
    @GetMapping("/{reservationId}")
    @MemberOnly
    public ResponseEntity<ReservationDetailResponse> getReservation(@Auth Accessor accessor,
                                                                    @PathVariable(value = "reservationId") Long reservationId) {
        return ResponseEntity.ok(reservationService.getReservationDetail(accessor.getMemberId(), reservationId));
    }

    @Operation(summary = "예약 취소")
    @PatchMapping("/{reservationId}")
    @MemberOnly
    public ResponseEntity<Void> cancelReservation(@Auth Accessor accessor,
                                                  @PathVariable(value = "reservationId") Long reservationId) {
        reservationService.cancelReservation(accessor.getMemberId(), reservationId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "예약 입금 확인 요청")
    @PatchMapping("/{reservationId}/payment")
    @MemberOnly
    public ResponseEntity<Void> requestPaymentConfirmation(@Auth Accessor accessor,
                                                           @PathVariable(value = "reservationId") Long reservationId,
                                                           @RequestBody @Valid ConfirmRequest request) {
        reservationService.requestPaymentConfirmation(accessor.getMemberId(), reservationId, request);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "예약 변경")
    @PatchMapping("/{reservationId}/time")
    @MemberOnly
    public ResponseEntity<Void> changeSchedule(@Auth Accessor accessor,
                                               @PathVariable(value = "reservationId") Long reservationId,
                                               @RequestBody @Valid ReservationUpdateRequest request){
        reservationService.rescheduleTimeSlot(accessor.getMemberId(), reservationId, request.timeSlotId());
        return ResponseEntity.noContent().build();
    }

}
