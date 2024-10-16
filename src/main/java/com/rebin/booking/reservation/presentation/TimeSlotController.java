package com.rebin.booking.reservation.presentation;

import com.rebin.booking.reservation.dto.request.TimeSlotResponse;
import com.rebin.booking.reservation.service.TimeSlotService;
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

    @GetMapping("")
    public ResponseEntity<List<TimeSlotResponse>> getTimeSlotByDate(@RequestParam LocalDate date) {
        return ResponseEntity.ok(timeSlotService.getTimeSlotsByDate(date));
    }
}
