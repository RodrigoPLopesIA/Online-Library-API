package br.com.rodrigo.onlinelibraryapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import br.com.rodrigo.onlinelibraryapi.exceptions.UnauthorizedException;
import br.com.rodrigo.onlinelibraryapi.services.EmailBookService;
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
    private EmailBookService emailService;

    @PostMapping
    public ResponseEntity postMethodName() {
        try {
            emailService.send("user@localhost", "test");
            return ResponseEntity.ok().build();
            
        } catch (Exception e) {
            throw new UnauthorizedException(e.getMessage());
        }
    }
    
}
