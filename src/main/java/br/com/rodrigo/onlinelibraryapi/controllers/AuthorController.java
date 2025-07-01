package br.com.rodrigo.onlinelibraryapi.controllers;

import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.rodrigo.onlinelibraryapi.dtos.author.ListAuthorDTO;
import br.com.rodrigo.onlinelibraryapi.services.AuthorService;

@RestController
@RequestMapping("api/v1/authors")
public class AuthorController {
    

    @Autowired
    private AuthorService authorService;


    @GetMapping
    public ResponseEntity<Page<ListAuthorDTO>> index(
            Pageable pageable,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam( name = "dateBirth", required = false) Date dateBirth) {
        Page<ListAuthorDTO> authors = authorService.index(pageable, name, dateBirth);
        return ResponseEntity.ok(authors);
    }
}
