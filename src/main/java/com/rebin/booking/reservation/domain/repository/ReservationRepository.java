package com.rebin.booking.reservation.domain.repository;

import com.rebin.booking.reservation.domain.Reservation;
import com.rebin.booking.reservation.domain.type.ReservationStatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends JpaRepository<Reservation,Long> {
    Optional<Reservation> findByCode(String code);
    boolean existsByMemberIdAndId(Long memberId, Long reservationId);
    boolean existsByMemberIdAndIdAndStatusIn(Long memberId, Long reservationId, List<ReservationStatusType> status);
    @Query("""
            select r from Reservation r where r.member.id = :memberId 
            and (r.status = 'PENDING_PAYMENT' or r.status = 'CONFIRM_REQUESTED'or r.status = 'PAYMENT_CONFIRMED')
            """)
    List<Reservation> findAllBeforeShootByMemberId(Long memberId);

    @Query("""
            select r from Reservation r where r.member.id = :memberId 
            and (r.status = 'SHOOTING_COMPLETED' or r.status = 'REVIEW_COMPLETED')
            """)
    List<Reservation> findAllAfterShootByMemberId(Long memberId);

    @Query("""
            select r from Reservation r where r.member.id = :memberId 
            and r.status = 'CANCELED'
            """)
    List<Reservation> findAllCanceledShootByMemberId(Long memberId);

    boolean existsByTimeSlotId(Long timeSlotId);

    int countByShootDateBetween(LocalDate startDate, LocalDate endDate);
    int countByProductId(Long productId);
}
