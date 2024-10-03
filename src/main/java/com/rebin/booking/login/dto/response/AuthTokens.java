package com.rebin.booking.login.dto.response;

public record AuthTokens(
        String accessToken,
        String refreshToken
) {
}
