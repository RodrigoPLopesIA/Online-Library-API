package br.com.rodrigo.onlinelibraryapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import br.com.rodrigo.onlinelibraryapi.exceptions.UnauthorizedException;
import br.com.rodrigo.onlinelibraryapi.services.EmailService;

import javax.naming.NameNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api/v1/mail")
public class EmailController {
    
    @Autowired
    private EmailService emailService;

    @PostMapping
    public ResponseEntity postMethodName() {
        try {
            emailService.sendHtmlEmailWithTemplate("user@localhost", "Test Email", "mail/welcome-email");
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }
    
}
