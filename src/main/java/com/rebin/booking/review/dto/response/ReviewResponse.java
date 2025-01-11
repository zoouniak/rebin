package com.rebin.booking.review.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.rebin.booking.product.dto.response.ProductResponse;
import com.rebin.booking.review.domain.Review;

import java.time.LocalDate;

public record ReviewResponse(
        Long reviewId,
        String nickname,
        String content,
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDate shootDate,
        int helpCnt,
        boolean isHelped,
        int commentCnt,
        ProductResponse productInfo

) {
    public static ReviewResponse of(Review review, int helpCnt, boolean isHelped){
        return new ReviewResponse(
                review.getId(),
                review.getMember().getNickname(),
                review.getContent(),
                review.getShootDate(),
                helpCnt,
                isHelped,
                review.getComments().size(),
                ProductResponse.of(review.getProduct())
        );
    }
}
