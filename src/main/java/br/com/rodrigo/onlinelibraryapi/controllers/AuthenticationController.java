package br.com.rodrigo.onlinelibraryapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.rodrigo.onlinelibraryapi.dtos.authentication.CredentialsDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;
import br.com.rodrigo.onlinelibraryapi.services.AuthenticationService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {


    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<TokenJWT> signin(@Valid @RequestBody CredentialsDTO credentials) {

        TokenJWT signin = authenticationService.authenticate(credentials);

        return ResponseEntity.ok().body(signin);

    }
}
