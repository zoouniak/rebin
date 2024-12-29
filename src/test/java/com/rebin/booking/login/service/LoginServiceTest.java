package com.rebin.booking.login.service;

import com.rebin.booking.common.excpetion.LoginException;
import com.rebin.booking.login.domain.RefreshToken;
import com.rebin.booking.login.domain.repository.RefreshTokenRepository;
import com.rebin.booking.login.dto.request.LoginRequest;
import com.rebin.booking.login.dto.response.AccessTokenResponse;
import com.rebin.booking.login.dto.response.AuthTokens;
import com.rebin.booking.login.dto.response.LoginResponse;
import com.rebin.booking.login.infra.JwtProvider;
import com.rebin.booking.login.infra.oauthProvider.OAuthProvider;
import com.rebin.booking.login.infra.oauthProvider.OAuthProviders;
import com.rebin.booking.login.infra.oauthUserInfo.OAuthUserInfo;
import com.rebin.booking.member.domain.Member;
import com.rebin.booking.member.domain.repository.MemberRepository;
import com.rebin.booking.member.type.ProviderType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.rebin.booking.common.excpetion.ErrorCode.FAIL_EXTEND;
import static com.rebin.booking.member.type.ProviderType.GOOGLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {
    @InjectMocks
    private LoginService loginService;
    @Mock
    private MemberRepository memberRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private OAuthProviders oAuthProviders;
    @Mock
    private JwtProvider jwtProvider;
    private static final String LOGIN_ID = "1";
    private static final String NICKNAME = "nickname";
    private static final String EMAIL = "email@email.com";
    private static final String PROVIDER = "google";
    private static final String ACCESS_TOKEN = "accessToken";
    private static final String REFRESH_TOKEN = "refreshToken";

    private static class TestOAuthProvider implements OAuthProvider {

        @Override
        public boolean equal(String provider) {
            return provider.equals(PROVIDER);
        }

        @Override
        public OAuthUserInfo getUserInfo(String code) {
            return new OAuthUserInfo() {
                @Override
                public String getLoginId() {
                    return LOGIN_ID;
                }

                @Override
                public String getNickname() {
                    return NICKNAME;
                }

                @Override
                public String getEmail() {
                    return EMAIL;
                }

                @Override
                public ProviderType getProvider() {
                    return GOOGLE;
                }
            };
        }
    }

    @Test
    void 새로운_사용자가_로그인을_한다() {
        // given
        when(oAuthProviders.mapping(PROVIDER))
                .thenReturn(new TestOAuthProvider());
        Member member = new Member(LOGIN_ID, EMAIL, NICKNAME, GOOGLE);
        AuthTokens expected = new AuthTokens(ACCESS_TOKEN, REFRESH_TOKEN);
        when(jwtProvider.generateLoginToken(anyString()))
                .thenReturn(expected);
        when(memberRepository.save(any()))
                .thenReturn(member);

        // when
        LoginResponse response = loginService.login(PROVIDER, new LoginRequest("code"));

        // then
        assertThat(response.tokens()).usingRecursiveComparison().isEqualTo(expected);
        verify(refreshTokenRepository).save(any());
    }

    @Test
    void 기존_사용자가_로그인을_한다() {
        // given
        when(oAuthProviders.mapping(PROVIDER))
                .thenReturn(new TestOAuthProvider());
        Member member = new Member(LOGIN_ID, EMAIL, NICKNAME, GOOGLE);
        AuthTokens expected = new AuthTokens(ACCESS_TOKEN, REFRESH_TOKEN);
        when(jwtProvider.generateLoginToken(anyString()))
                .thenReturn(expected);
        when(memberRepository.findByLoginId(any()))
                .thenReturn(Optional.of(member));

        // when
        LoginResponse response = loginService.login(PROVIDER, new LoginRequest("code"));

        // then
        assertThat(response.tokens()).usingRecursiveComparison().isEqualTo(expected);
        verify(refreshTokenRepository).save(any());
        verify(memberRepository, never()).save(any());
    }

    @Test
    @DisplayName("어세스토큰이 만료되었고 리프레시 토큰이 유효하면 새로운 어세스 토큰을 발급한다.")
    void extendSuccess_InvalidAccessTokenAndValidRefreshToken() {
        // given
        when(jwtProvider.resolveAccessToken(anyString()))
                .thenReturn(ACCESS_TOKEN);
        when(jwtProvider.isInvalidAccessTokenAndValidRefreshToken(ACCESS_TOKEN, REFRESH_TOKEN))
                .thenReturn(true);
        String expected = "newAccessToken";
        when(refreshTokenRepository.findById(anyString()))
                .thenReturn(Optional.of(new RefreshToken(REFRESH_TOKEN, Long.valueOf(LOGIN_ID))));
        when(jwtProvider.generateAccessToken(anyString()))
                .thenReturn(expected);

        // when
        AccessTokenResponse actual = loginService.extend("header", REFRESH_TOKEN);

        // then
        assertThat(actual.accessToken()).isEqualTo(expected);
    }

    @Test
    @DisplayName("어세스토큰이 유효하고 리프레시 토큰도 유효하면 기존 어세스 토큰을 반환한다.")
    void extendSuccess_validAccessTokenAndValidRefreshToken() {
        // given
        when(jwtProvider.resolveAccessToken(anyString()))
                .thenReturn(ACCESS_TOKEN);
        when(jwtProvider.isInvalidAccessTokenAndValidRefreshToken(ACCESS_TOKEN, REFRESH_TOKEN))
                .thenReturn(false);
        when(jwtProvider.isValidAccessTokenAndValidRefreshToken(ACCESS_TOKEN, REFRESH_TOKEN))
                .thenReturn(true);

        // when
        AccessTokenResponse actual = loginService.extend("header", REFRESH_TOKEN);

        // then
        assertThat(actual.accessToken()).isEqualTo(ACCESS_TOKEN);
    }

    @Test
    @DisplayName("어세스토큰, 리프레시 토큰이 유효하지 않거나 만료되었다면 에러가 발생한다.")
    void extendFail_InvalidAccessTokenOrInvalidRefreshToken() {
        // given
        when(jwtProvider.resolveAccessToken(anyString()))
                .thenReturn(ACCESS_TOKEN);
        when(jwtProvider.isInvalidAccessTokenAndValidRefreshToken(ACCESS_TOKEN, REFRESH_TOKEN))
                .thenReturn(false);
        when(jwtProvider.isValidAccessTokenAndValidRefreshToken(ACCESS_TOKEN, REFRESH_TOKEN))
                .thenReturn(false);

        // when
        assertThatThrownBy(() -> loginService.extend("header", REFRESH_TOKEN))
                .isInstanceOf(LoginException.class)
                .hasMessage(FAIL_EXTEND.getMsg());
    }

    @Test
    void 로그아웃한다(){
        // given
        doNothing().when(refreshTokenRepository).deleteById(anyString());

        // when
        loginService.removeRefreshToken(REFRESH_TOKEN);

        verify(refreshTokenRepository).deleteById(REFRESH_TOKEN);
    }

}