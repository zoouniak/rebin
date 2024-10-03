package com.rebin.booking.login.service;


import com.rebin.booking.common.excpetion.LoginException;
import com.rebin.booking.login.domain.RefreshToken;
import com.rebin.booking.login.domain.repository.RefreshTokenRepository;
import com.rebin.booking.login.dto.request.LoginRequest;
import com.rebin.booking.login.dto.response.AccessTokenResponse;
import com.rebin.booking.login.dto.response.AuthTokens;
import com.rebin.booking.login.infra.JwtProvider;
import com.rebin.booking.login.infra.oauthProvider.OAuthProvider;
import com.rebin.booking.login.infra.oauthProvider.OAuthProviders;
import com.rebin.booking.login.infra.oauthUserInfo.OAuthUserInfo;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.member.domain.repository.MemberRepository;
import com.rebin.booking.member.type.ProviderType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.rebin.booking.common.excpetion.ErrorCode.FAIL_EXTEND;
import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_REFRESH_TOKEN;


@Service
@RequiredArgsConstructor
public class LoginService {
    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuthProviders oAuthProviders;
    private final JwtProvider jwtProvider;

    public AuthTokens login(final String provider, final LoginRequest loginRequest) {
        final OAuthProvider strategy = oAuthProviders.mapping(provider);
        final OAuthUserInfo userInfo = strategy.getUserInfo(loginRequest.code());
        // 획득한 사용자 정보 db에 조회
        Member loginUser = findOrCreateUser(
                userInfo.getLoginId(),
                userInfo.getEmail(),
                userInfo.getProvider()
        );
        // accesstoken, refreshtoken 발급
        final AuthTokens loginTokens = jwtProvider.generateLoginToken(String.valueOf(loginUser.getId()));
        // refreshtoken 저장
        refreshTokenRepository.save(new RefreshToken(loginTokens.refreshToken(), loginUser.getId()));

        return loginTokens;

    }

    public AccessTokenResponse extend(String authorizeHeader, String refreshTokenReq) {
        final String accessToken = jwtProvider.resolveAccessToken(authorizeHeader);
        // isInvalidAccessToken && isValidRefreshToken -> then re-issue accessToken
        if (jwtProvider.isInvalidAccessTokenAndValidRefreshToken(accessToken, refreshTokenReq)) {
            RefreshToken refreshToken = refreshTokenRepository.findById(refreshTokenReq).orElseThrow(
                    () -> new LoginException(INVALID_REFRESH_TOKEN)
            );
            final String regenerateAccessToken = jwtProvider.generateAccessToken(refreshToken.getLoginId().toString());

            return new AccessTokenResponse(regenerateAccessToken);
        }

        // isValidAccessToken && isValidRefreshToken then return accessToken
        else if (jwtProvider.isValidAccessTokenAndValidRefreshToken(accessToken, refreshTokenReq)) {
            return new AccessTokenResponse(accessToken);
        }

        throw new LoginException(FAIL_EXTEND);
    }


    public void removeRefreshToken(String refreshToken) {
        refreshTokenRepository.deleteById(refreshToken);
    }

    private Member findOrCreateUser(final String loginId, final String email, ProviderType provider) {
        return memberRepository.findByLoginId(loginId)
                .orElseGet(() -> createUser(loginId, email, provider));
    }

    private Member createUser(final String loginId, final String email, ProviderType provider) {
        return memberRepository.save(new Member(loginId, email, provider));
    }
}
