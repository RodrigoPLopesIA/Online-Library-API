package br.com.rodrigo.onlinelibraryapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.view.RedirectView;

import br.com.rodrigo.onlinelibraryapi.dtos.authentication.CredentialsDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;
import br.com.rodrigo.onlinelibraryapi.services.AuthenticationService;
import br.com.rodrigo.onlinelibraryapi.services.GoogleAuthenticationService;
import br.com.rodrigo.onlinelibraryapi.services.GoogleCredentialDTO;
import br.com.rodrigo.onlinelibraryapi.services.strategy.AuthenticationStrategy;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    @Qualifier("normalAuth")
    private AuthenticationStrategy<CredentialsDTO> authenticationService;

    @Autowired
    @Qualifier("googleAuth")
    private AuthenticationStrategy<GoogleCredentialDTO> googleAuthenticationService;


    @PostMapping("login")
    public ResponseEntity<TokenJWT> signin(@Valid @RequestBody CredentialsDTO credentials) {

        TokenJWT signin = authenticationService.authenticate(credentials);

        return ResponseEntity.ok().body(signin);

    }

    @PostMapping("google")
    public ResponseEntity<TokenJWT> googleSignin(@Valid @RequestBody GoogleCredentialDTO credentials) {

        TokenJWT signin = googleAuthenticationService.authenticate(credentials);

        return ResponseEntity.ok().body(signin);

    }
}
