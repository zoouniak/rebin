package com.rebin.booking.reservation.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rebin.booking.reservation.domain.TimeSlot;

import java.time.LocalDate;
import java.time.LocalTime;

public record TimeSlotResponse(
        Long id,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate date,
        @JsonFormat(pattern = "HH:mm:ss")
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
