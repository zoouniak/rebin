package com.rebin.booking.login.presentation;


import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.login.dto.request.LoginRequest;
import com.rebin.booking.login.dto.response.AccessTokenResponse;
import com.rebin.booking.login.dto.response.AuthTokens;
import com.rebin.booking.login.service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private static final String REFRESH_TOKEN = "refresh-token";
    private static final String ACCESS_TOKEN = "access-token";
    private static final int COOKIE_AGE_SECONDS = 604800;
    private final LoginService loginService;

    /*
     * authorize code와 함께 로그인 요청
     */
    @PostMapping("/login/{provider}")
    public ResponseEntity<AccessTokenResponse> login(
            @PathVariable(name = "provider") final String provider,
            @RequestBody final LoginRequest loginRequest
    ) {
        AuthTokens tokens = loginService.login(provider, loginRequest);

        return ResponseEntity.
                status(CREATED)
                .header(SET_COOKIE, makeCookie(tokens.refreshToken()).toString())
                .header(ACCESS_TOKEN, tokens.accessToken())
                .build();
    }

    @PostMapping("/login/extend")
    public ResponseEntity<?> extendLogin(
            @CookieValue(name = REFRESH_TOKEN) final String refreshToken,
            @RequestHeader(name = AUTHORIZATION) final String authorizeHeader

    ) {
        AccessTokenResponse accessToken = loginService.extend(authorizeHeader, refreshToken);
        return ResponseEntity.status(CREATED)
                .header(ACCESS_TOKEN, accessToken.accessToken())
                .build();
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@Auth Accessor accessor, @CookieValue(name = REFRESH_TOKEN) final String refreshToken) {
        loginService.removeRefreshToken(refreshToken);
        return ResponseEntity.noContent().build();
    }

    private static ResponseCookie makeCookie(final String refreshToken) {

        return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .maxAge(COOKIE_AGE_SECONDS)
                .sameSite("None")
                .secure(true)
                .httpOnly(true)
                .path("/")
                .build();
    }
}