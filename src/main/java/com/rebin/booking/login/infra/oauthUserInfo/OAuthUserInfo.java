package com.rebin.booking.login.infra.oauthUserInfo;

import com.rebin.booking.member.type.ProviderType;

public interface OAuthUserInfo {
    String getLoginId();
    String getNickname();
    String getEmail();
    ProviderType getProvider();
}
