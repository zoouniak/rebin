package com.rebin.booking.reservation.domain.repository;

import com.rebin.booking.reservation.dto.response.TimeSlotResponseForAdmin;

import java.time.LocalDate;
import java.util.List;

public interface CustomTimeSlotRepository {
    List<TimeSlotResponseForAdmin> findAllByDate(LocalDate date);

}
