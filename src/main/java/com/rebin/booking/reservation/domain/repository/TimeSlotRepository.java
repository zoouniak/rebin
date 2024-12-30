package com.rebin.booking.reservation.domain.repository;

import com.rebin.booking.reservation.domain.TimeSlot;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    List<TimeSlot> findAllByDate(LocalDate date);

    boolean existsByDateAndStartTime(LocalDate date, LocalTime time);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ts FROM TimeSlot ts where ts.id = :id")
    Optional<TimeSlot> findByIdWithTimeSlot(@Param("id") Long id);
}
