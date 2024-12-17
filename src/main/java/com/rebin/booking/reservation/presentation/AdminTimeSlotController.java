package com.rebin.booking.reservation.presentation;

import com.rebin.booking.reservation.dto.request.TimeSlotRequest;
import com.rebin.booking.reservation.dto.response.TimeSlotResponse;
import com.rebin.booking.reservation.dto.response.TimeSlotResponseForAdmin;
import com.rebin.booking.reservation.service.AdminTimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

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

    @GetMapping
    @Operation(summary = "날짜별 타임슬롯 조회")
    public ResponseEntity<List<TimeSlotResponseForAdmin>> getTimeSlots(@RequestParam LocalDate date) {
        return ResponseEntity.ok(adminTimeSlotService.getTimeSlots(date));
    }

    @DeleteMapping("/{timeSlotId}")
    @Operation(summary = "타임슬롯 삭제")
    public ResponseEntity<Void> deleteTimeSlot(@PathVariable Long timeSlotId) {
        adminTimeSlotService.deleteTimeSlotById(timeSlotId);
        return ResponseEntity.noContent().build();
    }
}
