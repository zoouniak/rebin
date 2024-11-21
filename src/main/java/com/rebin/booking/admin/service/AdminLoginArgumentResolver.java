package com.rebin.booking.admin.service;


import com.rebin.booking.admin.domain.repository.AdminRepository;
import com.rebin.booking.auth.domain.Accessor;
import com.rebin.booking.auth.domain.AdminAuth;
import com.rebin.booking.common.excpetion.AuthException;
import com.rebin.booking.login.domain.repository.RefreshTokenRepository;
import com.rebin.booking.login.dto.response.AuthTokens;
import com.rebin.booking.login.infra.JwtProvider;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Arrays;

import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_REFRESH_TOKEN;
import static com.rebin.booking.common.excpetion.ErrorCode.INVALID_REQUEST;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@RequiredArgsConstructor
@Component
public class AdminLoginArgumentResolver implements HandlerMethodArgumentResolver {

    private static final String REFRESH_TOKEN = "refresh-token";

    private final JwtProvider jwtProvider;

    private final RefreshTokenRepository refreshTokenRepository;
    private final AdminRepository adminRepository;

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.withContainingClass(Long.class)
                .hasParameterAnnotation(AdminAuth.class);
    }

    @Override
    public Accessor resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        if (request == null) {
            throw new AuthException(INVALID_REQUEST);
        }

        final String refreshToken = extractRefreshToken(request.getCookies());
        final String accessToken = jwtProvider.resolveAccessToken(webRequest.getHeader(AUTHORIZATION));
        jwtProvider.validateTokens(new AuthTokens(refreshToken, accessToken));

        final Long adminId = Long.valueOf(jwtProvider.getUserIdFromToken(accessToken));

        if (adminRepository.findById(adminId).isPresent())
            return Accessor.admin(adminId);

        throw new AuthException(INVALID_REQUEST);

    }

    private String extractRefreshToken(final Cookie... cookies) {
        if (cookies == null) {
            throw new AuthException(INVALID_REFRESH_TOKEN);
        }
        return Arrays.stream(cookies)
                .filter(this::isValidRefreshToken)
                .findFirst()
                .orElseThrow(() -> new AuthException(INVALID_REFRESH_TOKEN))
                .getValue();
    }

    private boolean isValidRefreshToken(final Cookie cookie) {
        return REFRESH_TOKEN.equals(cookie.getName()) &&
                refreshTokenRepository.existsById(cookie.getValue());
    }
}
