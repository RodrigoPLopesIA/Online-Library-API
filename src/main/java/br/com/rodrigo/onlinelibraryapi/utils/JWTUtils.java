package br.com.rodrigo.onlinelibraryapi.utils;

import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;

@Slf4j
@NoArgsConstructor
public class JWTUtils {

    private static final String JWT_BEARER = "Bearer ";
    private static final String JWT_AUTHORIZATION = "Authentication";
    private static final String JWT_SECRET_KEY = "123456789012345678901234567890";
    private static final Long JWT_EXPIRATION_DAY = 0L;
    private static final Long JWT_EXPIRATION_HOUR = 0L;
    private static final Long JWT_EXPIRATION_MINUTES = 2L;

    public static Key generateKey() {
        return Keys.hmacShaKeyFor(JWT_SECRET_KEY.getBytes());
    }

    public static Date expiresDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(JWT_EXPIRATION_DAY)
                .plusHours(JWT_EXPIRATION_HOUR)
                .plusMinutes(JWT_EXPIRATION_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static TokenJWT createToken(String username) {
        Date now = new Date();
        Date expirationDate = expiresDate(now);
        Key key = generateKey();

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expirationDate)
                .signWith(key)
                .compact();

        return new TokenJWT(token);
    }

    private static Claims getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(generateKey())
                    .build()
                    .parseClaimsJws(refactorToken(token))
                    .getBody();
        } catch (JwtException e) {
            log.error(String.format("Token invalido s%", e.getMessage()));
        }
        return null;
    }

    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith((SecretKey) generateKey()).build().parseSignedClaims(refactorToken(token));
            return true;

        } catch (Exception e) {
            log.error(String.format("Invalid token %s", e.getMessage()));
        }
        return false;
    }

    private static String refactorToken(String token) {
        if (token.contains(JWT_BEARER)) {
            return token.substring(JWT_BEARER.length());
        }
        return token;
    }

}
