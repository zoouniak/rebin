package com.rebin.booking.login.infra.oauthProvider;


import com.rebin.booking.common.excpetion.LoginException;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.rebin.booking.common.excpetion.ErrorCode.NOT_SUPPORT_PROVIDER;


@Component
public class OAuthProviders {
    private final List<OAuthProvider> strategies;

    public OAuthProviders(List<OAuthProvider> strategies) {
        this.strategies = strategies;
    }

    public OAuthProvider mapping(final String providerName) {
        return strategies.stream().filter(
                provider -> provider.equal(providerName)
        ).findFirst().orElseThrow(() -> new LoginException(NOT_SUPPORT_PROVIDER));
    }
}
