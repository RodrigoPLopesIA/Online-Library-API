package br.com.rodrigo.onlinelibraryapi.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rodrigo.onlinelibraryapi.dtos.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.ListBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.services.BookService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("books")
public class BookController {



    @Autowired
    private BookService bookService;




    @PostMapping
    public ResponseEntity<ListBookDTO> create(@Valid @RequestBody CreateBookDTO data, UriComponentsBuilder uri) {
        Book book = bookService.create(data);

        URI uriBuilder = uri.path("/books/{id}").buildAndExpand(book.getId()).toUri();
        

        return ResponseEntity.created(uriBuilder).body(new ListBookDTO(book));
    }
    
}
