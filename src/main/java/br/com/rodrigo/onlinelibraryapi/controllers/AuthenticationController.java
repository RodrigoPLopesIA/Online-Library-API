package br.com.rodrigo.onlinelibraryapi.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.result.view.RedirectView;

import br.com.rodrigo.onlinelibraryapi.dtos.authentication.CredentialsDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.authentication.GoogleCredentialDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.books.ListBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;
import br.com.rodrigo.onlinelibraryapi.exceptions.UniqueViolationException;
import br.com.rodrigo.onlinelibraryapi.services.strategy.AuthenticationStrategy;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Tag(name = "Authentication", description = "managing authentication-related operations in the Online Library API. Provides endpoints to authenticate by social (google) and username/password.")
@SecurityRequirement(name = "") 
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    @Qualifier("normalAuth")
    private AuthenticationStrategy<CredentialsDTO> authenticationService;

    @Autowired
    @Qualifier("googleAuth")
    private AuthenticationStrategy<GoogleCredentialDTO> googleAuthenticationService;

    @Operation(summary = "SignIn with username and passowrd", responses = {
            @ApiResponse(responseCode = "200", description = "SignIn with a user by username/password", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CredentialsDTO.class))),
    })
    @PostMapping("login")
    public ResponseEntity<TokenJWT> signin(@Valid @RequestBody CredentialsDTO credentials) {

        TokenJWT signin = authenticationService.authenticate(credentials);

        return ResponseEntity.ok().body(signin);

    }
    @Operation(summary = "SignIn with social (google)", responses = {
            @ApiResponse(responseCode = "200", description = "SignIn with a user by social (google)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = GoogleCredentialDTO.class))),
    })
    @PostMapping("google")
    public ResponseEntity<TokenJWT> googleSignin(@Valid @RequestBody GoogleCredentialDTO credentials) {

        TokenJWT signin = googleAuthenticationService.authenticate(credentials);

        return ResponseEntity.ok().body(signin);

    }
}
