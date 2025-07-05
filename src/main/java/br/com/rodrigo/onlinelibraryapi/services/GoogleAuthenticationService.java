package br.com.rodrigo.onlinelibraryapi.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;

import br.com.rodrigo.onlinelibraryapi.dtos.authentication.CredentialsDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.repositories.UserRepository;
import br.com.rodrigo.onlinelibraryapi.services.strategy.AuthenticationStrategy;
import br.com.rodrigo.onlinelibraryapi.utils.JWTUtils;

public class GoogleAuthenticationService extends AuthenticationStrategy<OAuth2AuthenticationToken> {
    @Autowired
    private OAuth2AuthorizedClientService clientService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public TokenJWT authenticate(OAuth2AuthenticationToken input) {

        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                input.getAuthorizedClientRegistrationId(),
                input.getName());

        String userEmail = (String) client.getPrincipalName();
        String provider2= input.getAuthorizedClientRegistrationId();

        User existingUser = userRepository.findByAuthenticationEmail(userEmail);
        if (existingUser != null) {
            User user = new User();
            user.getAuthentication().setEmail(userEmail);
            user.getAuthentication().setProvider(provider2);
            userRepository.save(user);
        }

        User authenticateUser = this.authenticateUser(new CredentialsDTO(userEmail, provider2));
        TokenJWT token = JWTUtils.createToken(authenticateUser.getUsername());

        return token;
        
    }
    
}
