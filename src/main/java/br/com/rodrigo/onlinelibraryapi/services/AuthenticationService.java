package br.com.rodrigo.onlinelibraryapi.services;

import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.authentication.CredentialsDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.services.strategy.AuthenticationStrategy;
import br.com.rodrigo.onlinelibraryapi.utils.JWTUtils;

@Service("normalAuth")
public class AuthenticationService extends AuthenticationStrategy<CredentialsDTO> {

    @Override
    public TokenJWT authenticate(CredentialsDTO input) {

        User user = this.authenticateUser(input);
        TokenJWT token = JWTUtils.createToken(user.getUsername());

        return new TokenJWT(token.token(), token.expiresIn());

    }
}
