package com.rebin.booking.reservation.service;

import com.rebin.booking.common.excpetion.TimeSlotException;
import com.rebin.booking.reservation.domain.TimeSlot;
import com.rebin.booking.reservation.domain.repository.TimeSlotRepository;
import com.rebin.booking.reservation.dto.response.TimeSlotResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.rebin.booking.common.excpetion.ErrorCode.ALREADY_EXIST;

@Service
@RequiredArgsConstructor
public class AdminTimeSlotService {
    private final TimeSlotRepository timeSlotRepository;

    public TimeSlotResponse CreateTimeSlot(LocalDate date,LocalTime time ) {
        if (timeSlotRepository.existsByDateAndStartTime(date, time))
            throw new TimeSlotException(ALREADY_EXIST);

        TimeSlot timeSlot = new TimeSlot(date, time);
        TimeSlot save = timeSlotRepository.save(timeSlot);
        return TimeSlotResponse.of(save);
    }
}