package com.rebin.booking.reservation.presentation;

import com.rebin.booking.reservation.dto.request.TimeSlotRequest;
import com.rebin.booking.reservation.dto.response.TimeSlotResponse;
import com.rebin.booking.reservation.service.AdminTimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/timeslots")
@RequiredArgsConstructor
public class AdminTimeSlotController {
    private final AdminTimeSlotService adminTimeSlotService;

    @PostMapping
    @Operation(summary = "타임슬롯 생성")
    public ResponseEntity<TimeSlotResponse> createTimeSlot(@RequestBody TimeSlotRequest request) {
        return ResponseEntity.ok(adminTimeSlotService.CreateTimeSlot(request.date(), request.time()));
    }
}
