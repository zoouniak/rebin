package com.rebin.booking.reservation.dto.request;

import jakarta.validation.constraints.*;

public record ReservationRequest(
        @NotNull(message = "이름을 입력해주세요.")
        String name,
        @NotNull(message = "이메일은 필수값입니다.")
        String email,
        @Pattern(regexp = "^\\d{10,11}$", message = "전화번호는 10자리 또는 11자리 숫자여야 합니다.")
        String phone,
        @Positive(message = "인원 수는 1명 이상이여야 합니다.")
        int peopleCnt,
        @Size(max = 500, message = "요청사항의 길이는 500자 이하여야 합니다.")
        String notes,
        @NotNull(message = "인스타 업로드 동의 여부는 필수값입니다.")
        boolean agreeToInstaUpload,
        @AssertTrue(message = "개인정보 동의는 필수입니다")
        boolean agreeToPrivacyPolicy,
        @NotNull(message = "상품 id는 필수값입니다.")
        Long productId,
        @NotNull(message = "타임슬롯 id는 필수값입니다.")
        Long timeSlotId

) {
}
