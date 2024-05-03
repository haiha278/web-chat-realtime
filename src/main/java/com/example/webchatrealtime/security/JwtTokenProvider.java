package com.example.webchatrealtime.security;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class JwtTokenProvider {
    @Value("${auth.jwt.secret}")
    private String SECRET_KEY;
    @Value("${auth.jwt.expiration}")
    private Long JWT_EXPIRATION;
    @Value("${auth.jwt.refresh-secret}")
    private String REFRESH_SECRET_KEY;
    @Value("${auth.jwt.refresh-expiration}")
    private Long JWT_REFRESH_EXPIRATION;

    public String generateToken(CustomUserDetail customUserDetail) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JWT_EXPIRATION);
        return Jwts.builder()
                .setSubject(customUserDetail.getUsername())
                .setExpiration(expiration)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("invalid jwt token!");
        } catch (ExpiredJwtException ex) {
            log.error("expired jwt token");
        } catch (UnsupportedJwtException ex) {
            log.error("unsupported jwt token. Cannot parse");
        } catch (IllegalArgumentException ex) {
            log.error("jwt claims string is empty");
        }
        return false;
    }

    public String getUserNameFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public String generateRefreshToken(CustomUserDetail customUserDetail) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + JWT_REFRESH_EXPIRATION);
        return Jwts.builder()
                .setSubject(customUserDetail.getUsername())
                .setExpiration(expiration)
                .setIssuedAt(now)
                .signWith(SignatureAlgorithm.HS256, REFRESH_SECRET_KEY)
                .compact();
    }

    public boolean validateRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(REFRESH_SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("invalid jwt token!");
        } catch (ExpiredJwtException ex) {
            log.error("expired jwt token");
        } catch (UnsupportedJwtException ex) {
            log.error("unsupported jwt token. Cannot parse");
        } catch (IllegalArgumentException ex) {
            log.error("jwt claims string is empty");
        }
        return false;
    }

    public String getUserNameFromRefreshJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(REFRESH_SECRET_KEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
