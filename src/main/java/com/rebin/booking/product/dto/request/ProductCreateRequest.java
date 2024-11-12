package com.rebin.booking.product.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ProductCreateRequest(
        @NotNull(message = "이름을 입력해주세요.")
        String name,
        @PositiveOrZero(message = "가격은 0원 이상이여야 합니다.")
        int price,
        @Size(max = 20, message = "요청사항의 길이는 20자 이하여야 합니다.")
        String summary,
        @Size(max = 2000, message = "상세설명 길이는 2000자 이하여야 합니다.")
        String description,
        String thumbnail,
        List<String> images,

        @PositiveOrZero(message = "가격은 0원 이상이여야 합니다.")
        int extraPersonFee,
        @Size(max = 2000, message = "가이드라인 길이는 2000자 이하여야 합니다.")
        String guideLine
) {
}
