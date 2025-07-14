package br.com.rodrigo.onlinelibraryapi.dtos.token;

import java.time.Instant;

public record TokenJWT(String token, Instant expiresIn) {
    
}
