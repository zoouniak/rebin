package com.rebin.booking.admin.service;

import com.rebin.booking.admin.config.PasswordEncoder;
import com.rebin.booking.admin.domain.Admin;
import com.rebin.booking.admin.domain.repository.AdminRepository;
import com.rebin.booking.admin.dto.request.AdminLoginRequest;
import com.rebin.booking.common.excpetion.AdminException;
import com.rebin.booking.common.excpetion.LoginException;
import com.rebin.booking.login.domain.RefreshToken;
import com.rebin.booking.login.domain.repository.RefreshTokenRepository;
import com.rebin.booking.login.dto.response.AccessTokenResponse;
import com.rebin.booking.login.dto.response.AuthTokens;
import com.rebin.booking.login.infra.JwtProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminLoginServiceTest {
    @InjectMocks
    private AdminLoginService loginService;
    @Mock
    private AdminRepository adminRepository;
    @Mock
    private RefreshTokenRepository refreshTokenRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtProvider jwtProvider;

    @Test
    void 비밀번호_불일치() {
        AdminLoginRequest request = new AdminLoginRequest("admin", "wrongPassword");
        Admin admin = new Admin(1L, "admin", "password");
        when(adminRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(request.password(), admin.getPassword()))
                .thenReturn(false);

        AdminException exception = assertThrows(AdminException.class, () -> loginService.login(request));
        Assertions.assertEquals("AD001", exception.getCode());
    }

    @Test
    void 로그인_성공() {
        // given
        AdminLoginRequest request = new AdminLoginRequest("admin", "password");
        Admin admin = new Admin(1L, "admin", "password");
        AuthTokens expected = new AuthTokens("access", "refresh");
        when(adminRepository.findByLoginId(Mockito.anyString()))
                .thenReturn(Optional.of(admin));
        when(passwordEncoder.matches(request.password(), admin.getPassword()))
                .thenReturn(true);
        when(jwtProvider.generateLoginToken(anyString()))
                .thenReturn(expected);

        // when
        AuthTokens actual = loginService.login(request);

        // then
        assertThat(actual).usingRecursiveComparison().isEqualTo(actual);
        verify(refreshTokenRepository, times(1)).save(any());

    }

    @DisplayName("refresh token이 모두 만료되지 않은 경우 새로운 access token을 리턴한다.")
    @Test
    void 로그인_연장1() {
        String authorizeHeader = "header";
        String accessToken = "accessToken";
        when(jwtProvider.resolveAccessToken(authorizeHeader))
                .thenReturn(accessToken);
        when(jwtProvider.isInvalidAccessTokenAndValidRefreshToken(eq(accessToken), anyString()))
                .thenReturn(true);
        RefreshToken refreshToken = new RefreshToken("refresh", 1L);
        when(refreshTokenRepository.findById(anyString()))
                .thenReturn(Optional.of(refreshToken));
        AccessTokenResponse expected = new AccessTokenResponse("newAccessToken");
        when(jwtProvider.generateAccessToken(anyString()))
                .thenReturn(expected.accessToken());

        AccessTokenResponse actual = loginService.extend(authorizeHeader, "refreshTokenReq");

        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("access token과 refresh token이 모두 만료되지 않은 경우 기존 access token을 리턴한다.")
    @Test
    void 로그인_연장2() {
        String authorizeHeader = "header";
        String accessToken = "accessToken";
        when(jwtProvider.resolveAccessToken(authorizeHeader))
                .thenReturn(accessToken);
        when(jwtProvider.isInvalidAccessTokenAndValidRefreshToken(eq(accessToken), anyString()))
                .thenReturn(false);
        when(jwtProvider.isValidAccessTokenAndValidRefreshToken(eq(accessToken), anyString()))
                .thenReturn(true);
        AccessTokenResponse expected = new AccessTokenResponse(accessToken);

        AccessTokenResponse actual = loginService.extend(authorizeHeader, "refreshTokenReq");

        Assertions.assertEquals(expected, actual);
    }

    @DisplayName("access token과 refresh token이 만료된 경우 에러가 발생한다.")
    @Test
    void 로그인_연장3() {
        String authorizeHeader = "header";
        String accessToken = "accessToken";
        when(jwtProvider.resolveAccessToken(authorizeHeader))
                .thenReturn(accessToken);
        when(jwtProvider.isInvalidAccessTokenAndValidRefreshToken(eq(accessToken), anyString()))
                .thenReturn(false);
        when(jwtProvider.isValidAccessTokenAndValidRefreshToken(eq(accessToken), anyString()))
                .thenReturn(false);


        LoginException actual = assertThrows(LoginException.class, () -> loginService.extend(authorizeHeader, "refreshTokenReq"));

        Assertions.assertEquals("L008",actual.getCode());
    }
}