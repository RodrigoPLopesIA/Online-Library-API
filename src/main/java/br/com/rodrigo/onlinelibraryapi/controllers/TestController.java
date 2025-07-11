package br.com.rodrigo.onlinelibraryapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/v1/test")
public class TestController {
    

    @GetMapping
    public ResponseEntity getMethodName() {
        return ResponseEntity.ok().build();
    }
    
}
