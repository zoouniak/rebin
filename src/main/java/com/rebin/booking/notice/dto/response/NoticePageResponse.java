package com.rebin.booking.notice.dto.response;

import java.util.List;

public record NoticePageResponse(
        int totalPages,
        List<NoticeResponse> notices
) {

}
