package br.com.rodrigo.onlinelibraryapi.services.strategy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import br.com.rodrigo.onlinelibraryapi.dtos.authentication.CredentialsDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;
import br.com.rodrigo.onlinelibraryapi.entities.User;

public abstract class  AuthenticationStrategy<T> {
    
    @Autowired
    private AuthenticationManager authenticationManager;

    public abstract TokenJWT authenticate(T input);


    public User authenticateUser(CredentialsDTO input){
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                input.email(), input.password());

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        return (User) authenticate.getPrincipal();
    }
}
