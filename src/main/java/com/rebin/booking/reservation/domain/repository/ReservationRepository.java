package com.rebin.booking.reservation.domain.repository;

import com.rebin.booking.reservation.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Optional<Reservation> findByCode(String code);
    @Query("""
            select r from Reservation r where r.member.id=:memberId 
            and r.status = 'PAID' or r.status = 'PENDING'
            """)
    List<Reservation> findAllBeforeShootByMemberId(Long memberId);

    @Query("""
            select r from Reservation r where r.member.id=:memberId 
            and r.status = 'COMPLETED'
            """)
    List<Reservation> findAllAfterShootByMemberId(Long memberId);

    @Query("""
            select r from Reservation r where r.member.id=:memberId 
            and r.status = 'CANCELED'
            """)
    List<Reservation> findAllCanceledShootByMemberId(Long memberId);


}
