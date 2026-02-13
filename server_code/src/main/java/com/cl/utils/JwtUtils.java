package com.cl.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtils {

    private final SecretKey key;
    private final long expireMillis;

    public JwtUtils(@Value("${jwt.secret:}") String secret, @Value("${jwt.expire-seconds:3600}") long expireSeconds) {
        String normalizedSecret = secret;
        if (StringUtils.isBlank(normalizedSecret)) {
            normalizedSecret = "change-me-change-me-change-me-change-me-32bytes";
        }
        this.key = Keys.hmacShaKeyFor(normalizedSecret.getBytes(StandardCharsets.UTF_8));
        long safeExpireSeconds = expireSeconds <= 0 ? 3600 : expireSeconds;
        this.expireMillis = safeExpireSeconds * 1000L;
    }

    public String generateToken(Long userId, String username, String tableName, String role) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expireMillis);
        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim("userId", userId)
                .claim("username", username)
                .claim("tableName", tableName)
                .claim("role", role)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseClaims(String token) {
        String normalized = normalizeToken(token);
        if (StringUtils.isBlank(normalized)) {
            return null;
        }
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(normalized).getBody();
        } catch (JwtException | IllegalArgumentException e) {
            return null;
        }
    }

    private String normalizeToken(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        String trimmed = token.trim();
        if (trimmed.regionMatches(true, 0, "Bearer ", 0, "Bearer ".length())) {
            return trimmed.substring("Bearer ".length()).trim();
        }
        return trimmed;
    }
}
