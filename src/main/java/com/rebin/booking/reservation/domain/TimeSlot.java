package com.rebin.booking.reservation.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
public class TimeSlot {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(updatable = false)
    private LocalDate date;

    @Column(updatable = false)
    private LocalTime startTime;

    @Column
    private boolean isAvailable;
}
