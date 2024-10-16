package com.rebin.booking.review.dto.response;

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
}
