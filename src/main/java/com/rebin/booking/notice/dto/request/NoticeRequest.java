package com.rebin.booking.notice.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NoticeRequest(
        @Size(max = 20, message = "제목의 길이는 20자 이하여야합니다")
        @NotNull
        String title,

        @Size(max = 500, message = "제목의 길이는 500자 이하여야합니다")
        @NotNull
        String content
) {
}
