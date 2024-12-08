package com.rebin.booking.reservation.domain.repository;

import com.rebin.booking.reservation.domain.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TimeSlotRepository extends JpaRepository<TimeSlot,Long> {
    List<TimeSlot> findAllByDate(LocalDate date);
    boolean existsByDateAndStartTime(LocalDate date, LocalTime time);
}
