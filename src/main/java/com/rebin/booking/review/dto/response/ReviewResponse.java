package com.rebin.booking.review.dto.response;

import com.rebin.booking.review.domain.Review;

import java.time.LocalDate;

public record ReviewResponse(
        Long reviewId,
        String nickname,
        String content,
        LocalDate shootDate,
        int helpCnt,
        boolean isHelped,
        int commentCnt

) {
    public static ReviewResponse of(Review review, int helpCnt, boolean isHelped){
        return new ReviewResponse(
                review.getId(),
                review.getMember().getNickname(),
                review.getContent(),
                review.getShootDate(),
                helpCnt,
                isHelped,
                review.getComments().size()
        );
    }
}
