package br.com.rodrigo.onlinelibraryapi.controllers;

import org.mapstruct.control.MappingControl.Use;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rodrigo.onlinelibraryapi.dtos.authentication.CredentialsDTO;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping
    public ResponseEntity signin(@Valid @RequestBody CredentialsDTO credentials) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                credentials.email(), credentials.password());

        Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);


        User user = (User) authenticate.getPrincipal();

        return ResponseEntity.ok().build();

    }
}
