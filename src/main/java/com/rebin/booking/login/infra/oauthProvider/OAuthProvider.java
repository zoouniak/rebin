package com.rebin.booking.login.infra.oauthProvider;

import com.rebin.booking.login.infra.oauthUserInfo.OAuthUserInfo;
import org.springframework.stereotype.Component;

@Component
public interface OAuthProvider {
     boolean equal(String provider);

     OAuthUserInfo getUserInfo(String code);

}
