package br.com.rodrigo.onlinelibraryapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.com.rodrigo.onlinelibraryapi.dtos.authentication.CredentialsDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.utils.JWTUtils;

@Service
public class AuthenticationService {

    @Autowired
    private AuthenticationManager authenticationManager;

    public TokenJWT authenticate(CredentialsDTO input) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                input.email(), input.password());

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User user =  (User) authenticate.getPrincipal();

        TokenJWT token = JWTUtils.createToken(user.getUsername());

        return new TokenJWT(token.token(), token.expiresIn());

    }
}
