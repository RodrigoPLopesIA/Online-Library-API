package br.com.rodrigo.onlinelibraryapi.utils;

import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Slf4j
@NoArgsConstructor
public class JWTUtils {

    private static final String JWT_SECRET_KEY = "123456789012345678901234567890";
    private static final Long JWT_EXPIRATION_DAY = 0L;
    private static final Long JWT_EXPIRATION_HOUR = 0L;
    private static final Long JWT_EXPIRATION_MINUTES = 2L;

    public static Date expiresDate(Date start) {
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(JWT_EXPIRATION_DAY)
                .plusHours(JWT_EXPIRATION_HOUR)
                .plusMinutes(JWT_EXPIRATION_MINUTES);
        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static TokenJWT createToken(String username) {
        try {
            Date expiresDate = expiresDate(new Date());
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET_KEY);
            String token = JWT.create()
                    .withSubject(username)
                    .withIssuer("Online library API")
                    .withExpiresAt(expiresDate)
                    .sign(algorithm);
            return new TokenJWT(token, expiresDate.toInstant());
        } catch (JWTCreationException e) {
            log.error(String.format("error token %e", e.getMessage()));
        }
        return null;
    }

    public static DecodedJWT decode(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET_KEY);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer("Online library API")
                    .build();

            DecodedJWT verify = verifier.verify(token);
            return verify;
        } catch (JWTVerificationException e) {
            log.error(String.format("error token %e", e.getMessage()));
        }
        return null;
    }

}
