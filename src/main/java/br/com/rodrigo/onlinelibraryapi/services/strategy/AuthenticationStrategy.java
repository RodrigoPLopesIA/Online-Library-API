package br.com.rodrigo.onlinelibraryapi.services.strategy;

import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;

public interface AuthenticationStrategy<T> {
    TokenJWT authenticate(T input);
}
