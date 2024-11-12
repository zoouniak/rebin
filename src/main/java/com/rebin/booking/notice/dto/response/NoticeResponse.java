package com.rebin.booking.notice.dto.response;

import com.rebin.booking.notice.domain.Notice;

public record NoticeResponse(
        Long id,
        String title,
        String content
) {
    public static NoticeResponse of(Notice notice){
        return new NoticeResponse(
                notice.getId(),
                notice.getTitle(),
                notice.getContent()
        );
    }
}
