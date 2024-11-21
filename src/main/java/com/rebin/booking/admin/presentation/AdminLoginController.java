package com.rebin.booking.admin.presentation;

import com.rebin.booking.admin.dto.request.AdminLoginRequest;
import com.rebin.booking.admin.service.AdminLoginService;
import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.Auth;
import com.rebin.booking.login.dto.response.AccessTokenResponse;
import com.rebin.booking.login.dto.response.AuthTokens;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.SET_COOKIE;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminLoginController {
    private static final String REFRESH_TOKEN = "refresh-token";
    private static final String ACCESS_TOKEN = "access-token";
    private static final int COOKIE_AGE_SECONDS = 604800;

    private final AdminLoginService adminLoginService;

    @Operation(summary = "로그인")
    @PostMapping("/login")
    public ResponseEntity<AccessTokenResponse> login(
            @RequestBody @Valid final AdminLoginRequest adminLoginRequest
    ) {
        final AuthTokens tokens = adminLoginService.login(adminLoginRequest);
        return ResponseEntity.
                status(CREATED)
                .header(SET_COOKIE, makeCookie(tokens.refreshToken()).toString())
                .header(ACCESS_TOKEN, tokens.accessToken())
                .build();
    }

    @Operation(summary = "로그인 연장")
    @PostMapping("/login/token")
    public ResponseEntity<?> extendLogin(
            @CookieValue(name = REFRESH_TOKEN) final String refreshToken,
            @RequestHeader(name = AUTHORIZATION) final String authorizeHeader

    ) {
        AccessTokenResponse accessToken = adminLoginService.extend(authorizeHeader, refreshToken);
        return ResponseEntity.status(CREATED)
                .header(ACCESS_TOKEN, accessToken.accessToken())
                .build();
    }

    @Operation(summary = "로그아웃")
    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@Auth Accessor accessor, @CookieValue(name = REFRESH_TOKEN) final String refreshToken) {
        adminLoginService.removeRefreshToken(refreshToken);
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
