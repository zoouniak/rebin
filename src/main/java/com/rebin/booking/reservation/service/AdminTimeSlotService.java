package com.rebin.booking.reservation.service;

import com.rebin.booking.common.excpetion.TimeSlotException;
import com.rebin.booking.reservation.domain.TimeSlot;
import com.rebin.booking.reservation.domain.repository.CustomTimeSlotRepository;
import com.rebin.booking.reservation.domain.repository.ReservationRepository;
import com.rebin.booking.reservation.domain.repository.TimeSlotRepository;
import com.rebin.booking.reservation.dto.response.TimeSlotResponse;
import com.rebin.booking.reservation.dto.response.TimeSlotResponseForAdmin;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static com.rebin.booking.common.excpetion.ErrorCode.ALREADY_EXIST;
import static com.rebin.booking.common.excpetion.ErrorCode.CANT_DELETE;

@Service
@RequiredArgsConstructor
public class AdminTimeSlotService {
    private final TimeSlotRepository timeSlotRepository;
    private final CustomTimeSlotRepository customTimeSlotRepository;
    private final ReservationRepository reservationRepository;

    public TimeSlotResponse CreateTimeSlot(LocalDate date, LocalTime time) {
        if (timeSlotRepository.existsByDateAndStartTime(date, time))
            throw new TimeSlotException(ALREADY_EXIST);

        TimeSlot timeSlot = new TimeSlot(date, time);
        TimeSlot save = timeSlotRepository.save(timeSlot);
        return TimeSlotResponse.of(save);
    }

    public List<TimeSlotResponseForAdmin> getTimeSlots(LocalDate date) {
        return customTimeSlotRepository.findAllByDate(date);
    }

    public void deleteTimeSlotById(final Long timeSlotId) {
        if (!canDelete(timeSlotId))
            throw new TimeSlotException(CANT_DELETE);

        timeSlotRepository.deleteById(timeSlotId);
    }

    private boolean canDelete(final Long timeSlotId) {
        return reservationRepository.existsByTimeSlotId(timeSlotId);
    }
}
