package com.rebin.booking.review.domain.repository;

import com.rebin.booking.review.domain.ReviewHelp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewHelpRepository extends JpaRepository<ReviewHelp, Long> {
    boolean existsByMemberIdAndReviewId(Long memberId, Long reviewId);
    int countByReviewId(Long reviewId);
    void deleteByMemberIdAndReviewId(Long memberId, Long reviewId);
}
