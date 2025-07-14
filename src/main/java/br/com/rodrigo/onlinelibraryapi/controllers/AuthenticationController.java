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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@Tag(name = "Authentication", description = "managing authentication-related operations in the Online Library API. Provides endpoints to authenticate by social (google) and username/password.")
@SecurityRequirement(name = "") 
@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @Operation(summary = "SignIn with username and passowrd", responses = {
            @ApiResponse(responseCode = "200", description = "SignIn with a user by username/password", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CredentialsDTO.class))),
    })

    @PostMapping("login")
    public ResponseEntity<TokenJWT> signin(@Valid @RequestBody CredentialsDTO credentials) {

        TokenJWT token = authenticationService.authenticate(credentials);

        return ResponseEntity.ok().body(token);

    }
}
