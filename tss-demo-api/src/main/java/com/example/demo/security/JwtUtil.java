package com.example.demo.security;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	private long expirationMillis = (long)3600000;
    // Secretキー
    private final Key secretKey = generateSecretKey();
    
    // Secretキー生成
    private Key generateSecretKey() {
        // io.jsonwebtoken.security.Keysより安全なキーを生成する
        return Keys.secretKeyFor(SignatureAlgorithm.HS256);
    }

    // JWT生成
    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setExpiration(new Date(System.currentTimeMillis() + expirationMillis))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    // JWT認証
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // 認証失敗
            return false;
        }
    }

    // JWTから情報を取得
    public String extractSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

}
