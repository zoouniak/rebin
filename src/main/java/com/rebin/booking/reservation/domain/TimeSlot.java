package com.rebin.booking.reservation.domain;

import com.rebin.booking.common.excpetion.ErrorCode;
import com.rebin.booking.common.excpetion.ReservationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Getter
@Entity
@Table( uniqueConstraints = @UniqueConstraint(columnNames = {"date", "startTime"}))
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    public TimeSlot(LocalDate date, LocalTime startTime) {
        this.date = date;
        this.startTime = startTime;
        this.isAvailable = true;
    }

    public void SetUnAvailable() {
        if (!this.isAvailable)
            throw new ReservationException(ErrorCode.RESERVATION_FULL);
        this.isAvailable = false;
    }

    public void SetAvailable() {
        this.isAvailable = true;
    }
}
