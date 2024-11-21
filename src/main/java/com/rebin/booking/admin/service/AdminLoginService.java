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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.rebin.booking.common.excpetion.ErrorCode.*;


@Service
@RequiredArgsConstructor
public class AdminLoginService {
    private final AdminRepository adminRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthTokens login(final AdminLoginRequest loginRequest) {
        Admin admin = adminRepository.findByLoginId(loginRequest.loginId()).orElseThrow();
        if (!passwordEncoder.matches(loginRequest.password(), admin.getPassword()))
            throw new AdminException(INVALID_ADMIN);

        final AuthTokens authTokens = jwtProvider.generateLoginToken(admin.getId().toString());
        refreshTokenRepository.save(new RefreshToken(authTokens.refreshToken(), admin.getId()));

        return authTokens;
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

}
