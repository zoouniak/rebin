package com.rebin.booking.login.infra;



import com.rebin.booking.common.excpetion.InvalidJwtException;
import com.rebin.booking.common.excpetion.JwtExpiredException;
import com.rebin.booking.login.dto.response.AuthTokens;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import static com.rebin.booking.common.excpetion.ErrorCode.*;


@Component
public class JwtProvider {
    private static final String EMPTY_SUBJECT = "";
    private static final String PREFIX = "${security.jwt.";
    private final Key secretKey;
    private final Long accessExpirationTime;
    private final Long refreshExpirationTime;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;


    public JwtProvider(
            @Value(PREFIX + "secret-key}") String secretKey,
            @Value(PREFIX + "access-expiration-time}") Long accessExpirationTime,
            @Value(PREFIX + "refresh-expiration-time}") Long refreshExpirationTime
    ) {
        byte[] keyBytes = Base64.getDecoder().decode(secretKey);
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);
        this.accessExpirationTime = accessExpirationTime;
        this.refreshExpirationTime = refreshExpirationTime;
    }

    public AuthTokens generateLoginToken(final String userId) {
        final String accessToken = createToken(userId, accessExpirationTime);
        final String refreshToken = createToken(EMPTY_SUBJECT, refreshExpirationTime);

        return new AuthTokens(accessToken, refreshToken);
    }

    public String generateAccessToken(final String userId) {
        return createToken(userId, accessExpirationTime);
    }

    private String createToken(final String userId, final Long tokenValidTime) {
        final Date now = new Date();

        return Jwts.builder()
                .setHeader(createHeader())
                .setSubject(userId)
                .signWith(secretKey, signatureAlgorithm)
                .setExpiration(createExpireDate(now, tokenValidTime))
                .compact();
    }

    public String getUserIdFromToken(String accessToken) {
        Claims claims = getClaims(accessToken);

        return claims.getSubject();
    }

    public String resolveAccessToken(String authHeader) {
        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return "";
    }

    public boolean isInvalidAccessTokenAndValidRefreshToken(String accessToken, String refreshToken) {
        return isTokenExpired(accessToken) && !isTokenExpired(refreshToken);
    }

    public boolean isValidAccessTokenAndValidRefreshToken(String accessToken, String refreshToken) {
        return !isTokenExpired(accessToken) && !isTokenExpired(refreshToken);
    }

    private boolean isTokenExpired(String token) {
        try {
            getClaims(token);
        } catch (JwtExpiredException e) {
            return true;
        }
        return false;
    }

    private Map<String, Object> createHeader() {
        return Map.of("alg", "HS256", "typ", "jwt");
    }


    private Date createExpireDate(final Date now, final Long expirationTime) {
        return new Date(now.getTime() + expirationTime);
    }

    private Claims getClaims(final String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            throw new JwtExpiredException(EXPIRED_ACCESS_TOKEN);
        } catch (JwtException e) {
            throw new InvalidJwtException(INVALID_TOKEN);
        }
    }


    public void validateTokens(AuthTokens authTokens) {
        if (isTokenExpired(authTokens.accessToken()))
            throw new JwtExpiredException(EXPIRED_ACCESS_TOKEN);
        if (isTokenExpired(authTokens.refreshToken()))
            throw new JwtExpiredException(EXPIRED_REFRESH_TOKEN);
    }
}
