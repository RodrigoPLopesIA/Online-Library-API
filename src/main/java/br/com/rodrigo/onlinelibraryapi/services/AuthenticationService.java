package br.com.rodrigo.onlinelibraryapi.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import br.com.rodrigo.onlinelibraryapi.dtos.authentication.CredentialsDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.services.strategy.AuthenticationStrategy;
import br.com.rodrigo.onlinelibraryapi.utils.JWTUtils;
import jakarta.validation.Valid;

@Service
public class AuthenticationService implements AuthenticationStrategy<CredentialsDTO>{

    @Autowired
    private AuthenticationManager authenticationManager;


    @Override
    public TokenJWT authenticate(CredentialsDTO input) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                input.email(), input.password());

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);

        User user = (User) authenticate.getPrincipal();

        TokenJWT token = JWTUtils.createToken(user.getUsername());

        return new TokenJWT(token.token(), token.expiresIn());
        
    }
}
