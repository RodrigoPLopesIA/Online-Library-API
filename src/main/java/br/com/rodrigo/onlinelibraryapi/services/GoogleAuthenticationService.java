package br.com.rodrigo.onlinelibraryapi.services;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.rodrigo.onlinelibraryapi.dtos.authentication.CredentialsDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.authentication.GoogleCredentialDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.token.TokenJWT;
import br.com.rodrigo.onlinelibraryapi.entities.User;
import br.com.rodrigo.onlinelibraryapi.repositories.UserRepository;
import br.com.rodrigo.onlinelibraryapi.services.strategy.AuthenticationStrategy;
import br.com.rodrigo.onlinelibraryapi.utils.JWTUtils;

@Service("googleAuth")
public class GoogleAuthenticationService extends AuthenticationStrategy<GoogleCredentialDTO> {

    @Value("${google.client-id}")
    private String clientId;

    @Value("${google.client-secret}")
    private String clientSecret;

    @Value("${google.redirect-uri}")
    private String redirectUri;

    @Autowired
    private UserRepository userRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public TokenJWT authenticate(GoogleCredentialDTO input) {
        String code = input.code();

        
        HttpHeaders tokenHeaders = new HttpHeaders();
        tokenHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String tokenRequestBody = String.format(
            "code=%s&client_id=%s&client_secret=%s&redirect_uri=%s&grant_type=authorization_code",
            code, clientId, clientSecret, redirectUri
        );

        HttpEntity<String> tokenRequest = new HttpEntity<>(tokenRequestBody, tokenHeaders);

        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
            "https://oauth2.googleapis.com/token",
            HttpMethod.POST,
            tokenRequest,
            Map.class
        );

        String accessToken = (String) tokenResponse.getBody().get("access_token");

        
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(accessToken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
            "https://www.googleapis.com/oauth2/v3/userinfo",
            HttpMethod.GET,
            userRequest,
            Map.class
        );

        Map<String, Object> userInfo = userInfoResponse.getBody();
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        
        User user = userRepository.findByAuthenticationEmail(email);
        if (user == null) {
            user = new User();
            user.getAuthentication().setEmail(email);
            user.getAuthentication().setProvider("google");
            user.getName().setFirst_name(name);
            userRepository.save(user);
        }
        
        User authenticated = this.authenticateUser(new CredentialsDTO(user.getUsername(), "1234"));
        
        TokenJWT token = JWTUtils.createToken(authenticated.getUsername());

        return token;
    }
    
}
