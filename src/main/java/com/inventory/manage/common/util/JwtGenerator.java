package com.inventory.manage.common.util;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;

@Component
public class JwtGenerator {
    private SecretKey secretKey;

    public JwtGenerator(@Value("${spring.jwt.secret}") String secret) {
        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8),
                Jwts.SIG.HS256.key().build().getAlgorithm());
    }

    public String generateJwt(String category, String username, String role, Long expiredMs) {
        return Jwts.builder()
                .claim("userId",
                        username) // 페이로드에 userId(email)추가
                .claim("role", role) // 페이로드에 role추가
                .issuedAt(new Date(System.currentTimeMillis())) // token이 발급되는 시점 기록
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료 기점 기록
                .signWith(secretKey) // 암호화 진행
                .compact(); // 토큰 최종 발행
    }
}
