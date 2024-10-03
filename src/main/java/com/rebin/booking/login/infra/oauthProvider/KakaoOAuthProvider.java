package com.rebin.booking.login.infra.oauthProvider;


import com.rebin.booking.common.excpetion.LoginException;
import com.rebin.booking.login.infra.OAuthAccessToken;
import com.rebin.booking.login.infra.oauthUserInfo.KakaoOAuthUserInfo;
import com.rebin.booking.login.infra.oauthUserInfo.OAuthUserInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


import static com.rebin.booking.common.excpetion.ErrorCode.FAIL_GET_USERINFO;
import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_AUTHORIZE_CODE;
import static org.springframework.http.HttpMethod.POST;

@Component
public class KakaoOAuthProvider implements OAuthProvider {
    private static final String prefix = "${oauth.provider.kakao.";
    private static final String providerName = "kakao";
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String tokenUri;
    private final String userUri;

    public KakaoOAuthProvider(@Value(prefix + "client-id}") String clientId,
                              @Value(prefix + "client-secret}") String clientSecret,
                              @Value(prefix + "redirect-uri}") String redirectUri,
                              @Value(prefix + "token-uri}") String tokenUri,
                              @Value(prefix + "user-uri}") String userUri) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.tokenUri = tokenUri;
        this.userUri = userUri;
    }

    @Override
    public boolean equal(final String provider) {
        return providerName.equals(provider);
    }


    private String getAccessToken(final String code) {
        final HttpEntity<MultiValueMap<String, String>> accessTokenRequestEntity = getAccessTokenRequestEntity(code);
        final ResponseEntity<OAuthAccessToken> accessToken = new RestTemplate().exchange(tokenUri, POST, accessTokenRequestEntity, OAuthAccessToken.class);

        return Optional.ofNullable(accessToken.getBody())
                .orElseThrow(() -> new LoginException(INVALID_AUTHORIZE_CODE))
                .accessToken();
    }

    private HttpEntity<MultiValueMap<String, String>> getAccessTokenRequestEntity(final String code) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");
        return new HttpEntity<>(params, headers);
    }

    private HttpEntity<MultiValueMap<String, String>> getUserInfoRequestEntity(final String token) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);
    }

    @Override
    public OAuthUserInfo getUserInfo(final String code) {
        // 카카오에 access token 요청
        String accessToken = getAccessToken(code);
        // access token으로 사용자 정보 요청
        final ResponseEntity<KakaoOAuthUserInfo> userInfo = new RestTemplate().exchange(
                userUri,
                HttpMethod.GET,
                getUserInfoRequestEntity(accessToken),
                KakaoOAuthUserInfo.class
        );

        // 사용자 정보 반환
        return Optional.ofNullable(userInfo.getBody())
                .orElseThrow(() -> new LoginException(FAIL_GET_USERINFO));
    }
}
