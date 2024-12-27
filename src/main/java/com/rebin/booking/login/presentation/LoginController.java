package com.rebin.booking.login.presentation;


import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.login.dto.request.LoginRequest;
import com.rebin.booking.login.dto.response.AccessTokenResponse;
import com.rebin.booking.login.dto.response.LoginResponse;
import com.rebin.booking.login.service.LoginService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LoginController {
    private static final String REFRESH_TOKEN = "refresh-token";
    private static final String ACCESS_TOKEN = "access-token";
    private static final int COOKIE_AGE_SECONDS = 604800;
    private final LoginService loginService;

    @Operation(summary = "소셜 로그인 (kakao, google)")
    @PostMapping("/login/{provider}")
    public ResponseEntity<Map<String,String>> login(
            @PathVariable(name = "provider") final String provider,
            @RequestBody final LoginRequest loginRequest
    ) {
        LoginResponse response = loginService.login(provider, loginRequest);

        return ResponseEntity.
                status(CREATED)
                .header(SET_COOKIE, makeCookie(response.tokens().refreshToken()).toString())
                .header(ACCESS_TOKEN, response.tokens().accessToken())
                .body(Map.of("nickname", response.nickname()));
    }

    @Operation(summary = "로그인 연장")
    @PostMapping("/login/extend")
    public ResponseEntity<Void> extendLogin(
            @CookieValue(name = REFRESH_TOKEN) final String refreshToken,
            @RequestHeader(name = AUTHORIZATION) final String authorizeHeader

    ) {
        AccessTokenResponse accessToken = loginService.extend(authorizeHeader, refreshToken);
        return ResponseEntity.status(CREATED)
                .header(ACCESS_TOKEN, accessToken.accessToken())
                .build();
    }

    @Operation(summary = "로그아웃")
    @DeleteMapping("/logout")
    public ResponseEntity<Void> logout(@Auth Accessor accessor, @CookieValue(name = REFRESH_TOKEN) final String refreshToken) {
        loginService.removeRefreshToken(refreshToken);
        return ResponseEntity.noContent().build();
    }

    private static ResponseCookie makeCookie(final String refreshToken) {

        return ResponseCookie.from(REFRESH_TOKEN, refreshToken)
                .maxAge(COOKIE_AGE_SECONDS)
                .sameSite("None")
                .secure(true)
                .httpOnly(false)
                .path("/")
                .build();
    }
}