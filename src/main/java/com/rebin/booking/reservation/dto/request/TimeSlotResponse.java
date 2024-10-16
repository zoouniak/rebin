package com.rebin.booking.reservation.dto.request;

import com.rebin.booking.reservation.domain.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeSlotResponse(
        Long id,
        LocalDate date,
        LocalTime startTime,
        boolean isAvailable
) {
    public static TimeSlotResponse of(TimeSlot timeSlot){
        return new TimeSlotResponse(
                timeSlot.getId(),
                timeSlot.getDate(),
                timeSlot.getStartTime(),
                timeSlot.isAvailable()
        );
    }
}
