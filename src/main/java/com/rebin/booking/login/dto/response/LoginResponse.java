package com.rebin.booking.login.dto.response;

public record LoginResponse(
        String nickname,
        AuthTokens tokens
) {
    public static LoginResponse from(String nickname, AuthTokens tokens){
        return new LoginResponse(nickname,tokens);
    }
}
