package com.rebin.booking.review.domain.repository;

import com.rebin.booking.review.domain.Review;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {


    @Query("""
            select review from Review review
            where review.product.id = :productId
            """)
    List<Review> findByProductIdAndPageable(@Param(value = "productId") Long productId, Pageable pageable);

    boolean existsByReservationId(Long reservationId);

    boolean existsByIdAndMemberId(Long reviewId, Long memberId);
}
