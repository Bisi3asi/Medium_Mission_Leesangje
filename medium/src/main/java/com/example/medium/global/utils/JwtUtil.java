package com.example.medium.global.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

@Component
@PropertySource("classpath:application-secret.properties")
public class JwtUtil {
    private static String SECRET_KEY;

    @Value("${jwt.secret-key}")
    public void setSECRET_Key(String value){
        SECRET_KEY = value;
    }

    public static String encodeToken(Map<String, Object> data, int minute){
        Date now = new Date();
        Date expiration = new Date(now.getTime() + 1000 * 60 * minute);

        Claims claims = Jwts.claims() // JWTmutator 사용
                .setSubject("medium")
                .add("type", "access_token")
                .add("data", data)
                .build();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public static Claims decodeToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getPayload();
        } catch (JwtException e) {
            System.out.printf("%s : Token has Expired", token);
            return null;
        }
    }
}
