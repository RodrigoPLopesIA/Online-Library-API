package br.com.rodrigo.onlinelibraryapi.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.books.ListBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.services.BookService;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@RestController
@RequestMapping("api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;
    
    @GetMapping
    public ResponseEntity<Page<ListBookDTO>> index(Pageable pageable) {
        Page<ListBookDTO> books = bookService.index(pageable);
        return ResponseEntity.ok().body(books);
    }

    @PostMapping
    public ResponseEntity<ListBookDTO> create(@Valid @RequestBody CreateBookDTO data, UriComponentsBuilder uri) {
        Book book = bookService.create(data);

        URI uriBuilder = uri.path("/books/{id}").buildAndExpand(book.getId()).toUri();
        

        return ResponseEntity.created(uriBuilder).body(new ListBookDTO(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListBookDTO> update(@PathVariable String id, @Valid @RequestBody CreateBookDTO data) {
        Book book = bookService.update(UUID.fromString(id), data);
        return ResponseEntity.ok().body(new ListBookDTO(book));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListBookDTO> show(@PathVariable String id) {
        return ResponseEntity.ok().body(new ListBookDTO(bookService.show(UUID.fromString(id))));
    }
    
}
