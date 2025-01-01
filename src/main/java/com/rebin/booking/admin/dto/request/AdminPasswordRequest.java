package com.rebin.booking.admin.dto.request;

public record AdminPasswordRequest(
        String originalPassword,
        String newPassword
) {
}
