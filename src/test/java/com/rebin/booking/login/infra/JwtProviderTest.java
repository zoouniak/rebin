package com.rebin.booking.login.infra;

import com.rebin.booking.common.excpetion.InvalidJwtException;
import com.rebin.booking.common.excpetion.JwtExpiredException;
import com.rebin.booking.login.dto.response.AuthTokens;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

import static com.rebin.booking.common.excpetion.ErrorCode.*;

@SpringBootTest
class JwtProviderTest {
    private static final String SUBJECT = "userId";
    private static final String INVALID_SECRET_KEY = "ujS9eTgHyooesYVEDYwAOZR5vhDKptomhcvsROwYaRV";
    private static final Long EXPIRATION_TIME = 60000L;
    private static final Long EXPIRED_TIME = 0L;
    @Autowired
    private JwtProvider jwtProvider;
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    @Test
    void 로그인_토큰을_생성한다() {
        // when
        AuthTokens authTokens = jwtProvider.generateLoginToken(SUBJECT);

        // then
        org.assertj.core.api.Assertions.assertThat(jwtProvider.getUserIdFromToken(authTokens.accessToken())).isEqualTo(SUBJECT);
        org.assertj.core.api.Assertions.assertThat(jwtProvider.getUserIdFromToken(authTokens.refreshToken())).isNull();
    }

    @Test
    void 토큰_검증을_통과한다() {
        AuthTokens authTokens = jwtProvider.generateLoginToken(SUBJECT);

        // then
        Assertions.assertDoesNotThrow(() -> jwtProvider.validateTokens(authTokens));
    }

    @Test
    @DisplayName("리프레시 토큰이 만료되었을 경우 에러가 발생한다.")
    void validateTokens_expiredRefreshToken() {
        String expiredRefreshToken = createTestJwt(SUBJECT, EXPIRED_TIME, secretKey);
        String accessToken = createTestJwt(SUBJECT, EXPIRATION_TIME, secretKey);
        AuthTokens authTokens = new AuthTokens(accessToken, expiredRefreshToken);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> jwtProvider.validateTokens(authTokens))
                .isInstanceOf(JwtExpiredException.class)
                .hasMessage(EXPIRED_REFRESH_TOKEN.getMsg());

    }

    @Test
    @DisplayName("어세스 토큰이 만료되었을 경우 에러가 발생한다.")
    void validateTokens_expiredAccessToken() {
        String refreshToken = createTestJwt(SUBJECT, EXPIRATION_TIME, secretKey);
        String expiredAccessToken = createTestJwt(SUBJECT, EXPIRED_TIME, secretKey);
        AuthTokens authTokens = new AuthTokens(expiredAccessToken, refreshToken);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> jwtProvider.validateTokens(authTokens))
                .isInstanceOf(JwtExpiredException.class)
                .hasMessage(EXPIRED_ACCESS_TOKEN.getMsg());
    }

    @Test
    @DisplayName("리프레시 토큰이 유효하지 않을 경우 에러가 발생한다.")
    void validateTokens_InvalidRefreshToken() {
        String InvalidRefreshToken = createTestJwt(SUBJECT, EXPIRATION_TIME, INVALID_SECRET_KEY);
        String accessToken = createTestJwt(SUBJECT, EXPIRATION_TIME, secretKey);
        AuthTokens authTokens = new AuthTokens(accessToken, InvalidRefreshToken);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> jwtProvider.validateTokens(authTokens))
                .isInstanceOf(InvalidJwtException.class)
                .hasMessage(INVALID_TOKEN.getMsg());
    }

    @Test
    @DisplayName("어세스 토큰이 유효하지 않을 경우 에러가 발생한다.")
    void validateTokens_InvalidAccessToken() {
        String refreshToken = createTestJwt(SUBJECT, EXPIRATION_TIME, secretKey);
        String InvalidAccessToken = createTestJwt(SUBJECT, EXPIRATION_TIME, INVALID_SECRET_KEY);
        AuthTokens authTokens = new AuthTokens(InvalidAccessToken, refreshToken);

        org.assertj.core.api.Assertions.assertThatThrownBy(() -> jwtProvider.validateTokens(authTokens))
                .isInstanceOf(InvalidJwtException.class)
                .hasMessage(INVALID_TOKEN.getMsg());
    }

    private String createTestJwt(final String subject, final Long expirationTime, final String secretKey) {
        final Date now = new Date();

        return Jwts.builder()
                .setHeader(Map.of("alg", "HS256", "typ", "jwt"))
                .setSubject(subject)
                .signWith(
                        Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256
                )
                .setExpiration(new Date(now.getTime() + expirationTime))
                .compact();
    }
}