package com.rebin.booking.login.infra;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuthAccessToken(
        @JsonProperty("access_token")
        String accessToken,
        @JsonProperty("token_type")
        String tokenType,
        @JsonProperty("refresh_token")
        String refreshToken,
        @JsonProperty("expires_in")
        int expiresIn,
        @JsonProperty("refresh_token_expires_in")
        int refreshTokenExpiresIn
) {

}
