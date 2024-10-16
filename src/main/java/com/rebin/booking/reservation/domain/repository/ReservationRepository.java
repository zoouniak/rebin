package com.rebin.booking.reservation.domain.repository;

import com.rebin.booking.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Optional<Reservation> findByCode(String code);
}
