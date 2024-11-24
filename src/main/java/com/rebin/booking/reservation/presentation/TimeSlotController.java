package com.rebin.booking.reservation.presentation;

import com.rebin.booking.reservation.dto.response.TimeSlotResponse;
import com.rebin.booking.reservation.service.TimeSlotService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/timeslots")
@RequiredArgsConstructor
public class TimeSlotController {
    private final TimeSlotService timeSlotService;

    @Operation(summary = "날짜별 타임슬롯 조회 date 파라미터")
    @GetMapping("")
    public ResponseEntity<List<TimeSlotResponse>> getTimeSlotByDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(timeSlotService.getTimeSlotsByDate(date));
    }
}
