package com.rebin.booking.login.infra.oauthUserInfo;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rebin.booking.member.type.ProviderType;

import static com.rebin.booking.member.type.ProviderType.GOOGLE;

public record GoogleOAuthUserInfo(
        @JsonProperty("id")
        String socialId,
        @JsonProperty("email")
        String email,
        @JsonProperty("name")
        String name
) implements OAuthUserInfo {
    @Override
    public String getLoginId() {
        return socialId();
    }

    @Override
    public String getNickname() {
        return name();
    }

    @Override
    public String getEmail() {
        return email();
    }

    @Override
    public ProviderType getProvider() {
        return GOOGLE;
    }


}
