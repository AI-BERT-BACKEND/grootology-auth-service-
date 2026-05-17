package com.aibert.dosw.application.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.UUID;

@Service
public class JwtTokenService {

    @Value("${jwt.secret}")
    private String secret;

    private static final long ONE_HOUR_MS = 3_600_000L;
    private static final long SEVEN_DAYS_MS = 7L * 24 * 3_600_000;

    public String generateToken(String email, UUID userId, String role, boolean rememberMe) {
        return generateToken(email, userId, role, rememberMe, null);
    }

    public String generateToken(String email, UUID userId, String role, boolean rememberMe, Integer passwordVersion) {
        long expiration = rememberMe ? SEVEN_DAYS_MS : ONE_HOUR_MS;
        Key key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        var builder = Jwts.builder()
                .setSubject(email)
                .claim("userId", userId.toString())
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration));
        if (passwordVersion != null) {
            builder.claim("passwordVersion", passwordVersion);
        }
        return builder.signWith(key, SignatureAlgorithm.HS256).compact();
    }
}
