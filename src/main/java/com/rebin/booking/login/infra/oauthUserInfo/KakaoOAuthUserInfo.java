package com.rebin.booking.login.infra.oauthUserInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rebin.booking.member.type.ProviderType;

import static com.rebin.booking.member.type.ProviderType.KAKAO;

public record KakaoOAuthUserInfo(
        @JsonProperty("id")
        String socialId,
        @JsonProperty("kakao_account")
        KakaoAccount kakaoAccount
) implements OAuthUserInfo {
    @Override
    public String getLoginId() {
        return socialId();
    }

    @Override
    public String getNickname() {
        return kakaoAccount.profile.nickname();
    }

    @Override
    public String getEmail() {
        return kakaoAccount.email;
    }

    @Override
    public ProviderType getProvider() {
        return KAKAO;
    }

    public record KakaoAccount(
            Profile profile,
            String email
    ) {
    }

    public record Profile(
            @JsonProperty("nickname")
            String nickname
    ) {
        public String nickname() {
            return nickname;
        }
    }
}
