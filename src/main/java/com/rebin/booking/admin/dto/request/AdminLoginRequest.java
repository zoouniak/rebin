package com.rebin.booking.admin.dto.request;

public record AdminLoginRequest(
        String loginId,
        String password
) {
}
