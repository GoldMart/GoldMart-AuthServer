package com.goldmarket.common.util;

import com.goldmarket.member.dto.CustomUserInfo;
import com.goldmarket.member.dto.JwtTokenRes;
import com.goldmarket.refresh_token.domain.RefreshToken;
import com.goldmarket.refresh_token.repository.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import java.security.Key;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtUtil {

    private final Key key;
    private final long accessTokenExpTime;
    private final long refreshTokenExpTime;
    private final RefreshTokenRepository refreshTokenRepository;

    public JwtUtil(
        @Value("${JWT_SECRET}") String secretKey,
        @Value("${JWT_ACCESS}") long accessTokenExpTime,
        @Value("${JWT_REFRESH}") long refreshTokenExpTime,
        RefreshTokenRepository refreshTokenRepository
    ) {
        this.refreshTokenRepository = refreshTokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenExpTime = accessTokenExpTime;
        this.refreshTokenExpTime = refreshTokenExpTime;
    }

    /*
     * Access Token & Refresh Token 생성
     * */
    public JwtTokenRes generateAccessAndRefreshToken(CustomUserInfo member) {
        return JwtTokenRes.builder()
            .grantType("Bearer")
            .accessToken(generateToken(member, "Access"))
            .refreshToken(generateToken(member, "Refresh"))
            .build();
    }

    /*
     * JWT 생성
     * */
    private String generateToken(CustomUserInfo member, String tokenType) {

        long expireTime = tokenType.equals("Access") ? accessTokenExpTime : refreshTokenExpTime;

        Claims claims = Jwts.claims();
        claims.put("id", member.getId());
        claims.put("name", member.getName());

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime tokenValidity = now.plusSeconds(expireTime);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(Date.from(now.toInstant()))
            .setExpiration(Date.from(tokenValidity.toInstant()))
            .signWith(key, SignatureAlgorithm.HS256)
            .compact();
    }

    /*
     * Token에서 User ID 추출
     * */
    public Long getUserId(String token) {
        return parseClaims(token).get("id", Long.class);
    }

    /*
     * JWT 검증
     * */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("유효하지 않은 JWT Token입니다.", e);
        } catch (ExpiredJwtException e) {
            log.info("만료된 JWT Token입니다.", e);
        } catch (UnsupportedJwtException e) {
            log.info("지원하지 않는 JWT Token입니다.", e);
        } catch (IllegalArgumentException e) {
            log.info("JWT claims이 비어 있습니다.", e);
        }
        return false;
    }

    public boolean validateRefreshToken(String token) {
        if (!validateToken(token)) {
            return false;
        }
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findById(getUserId(token));
        return refreshToken.isPresent() && token.equals(refreshToken.get().getToken());
    }

    /*
     * JWT Claims 추출
     * */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken)
                .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}
