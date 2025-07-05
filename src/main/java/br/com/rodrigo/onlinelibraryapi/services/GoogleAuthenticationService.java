package br.com.rodrigo.onlinelibraryapi.services;

import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;
import br.com.rodrigo.onlinelibraryapi.services.strategy.AuthenticationStrategy;

public class GoogleAuthenticationService extends AuthenticationStrategy<GoogleCredentialDTO> {

    @Override
    public TokenJWT authenticate(GoogleCredentialDTO input) {

        
        return new TokenJWT();
        
    }
    
}
