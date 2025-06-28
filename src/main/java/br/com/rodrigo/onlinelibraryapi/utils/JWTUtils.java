package br.com.rodrigo.onlinelibraryapi.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;

import br.com.rodrigo.onlinelibraryapi.dtos.TokenJWT;
import org.springframework.beans.factory.annotation.Value;

public class JWTUtils {


    private String secret;
//    public TokenJWT create() {
//        try {
////            Algorithm algorithm = Algorithm.RSA256(secret);
////            String token = JWT.create()
////                    .withIssuer("auth0")
////                    .sign(algorithm);
//        } catch (JWTCreationException exception) {
//            // Invalid Signing configuration / Couldn't convert Claims.
//        }
//    }
}
