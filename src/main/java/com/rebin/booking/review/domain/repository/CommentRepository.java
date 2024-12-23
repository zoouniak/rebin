package com.rebin.booking.review.domain.repository;

import com.rebin.booking.review.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    boolean existsByIdAndMemberId(Long commentId, Long memberId);
}
