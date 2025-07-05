package br.com.rodrigo.onlinelibraryapi.controllers;

import java.net.URI;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rodrigo.onlinelibraryapi.dtos.books.CreateBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.books.ListBookDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.user.ListUserDto;
import br.com.rodrigo.onlinelibraryapi.entities.Book;
import br.com.rodrigo.onlinelibraryapi.enums.Genre;
import br.com.rodrigo.onlinelibraryapi.mapper.BookMapper;
import br.com.rodrigo.onlinelibraryapi.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;

@Tag(name = "Books", description = "managing user-related operations in the Online Library API. Provides endpoints to create, retrieve, update, and delete books.")
@RestController
@RequestMapping("api/v1/books")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookMapper mapper;

    @Operation(summary = "Get all Books", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of books", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ListUserDto.class))))
    })
    @GetMapping
    public ResponseEntity<Page<ListBookDTO>> index(@RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "isbn", required = false) String isbn,
            @RequestParam(value = "authorName", required = false) String authorName,
            @RequestParam(value = "genre", required = false) Genre genre,
            @RequestParam(value = "nationality", required = false) String nationality,
            Pageable pageable) {
        Page<ListBookDTO> books = bookService.index(pageable, title, isbn, authorName, genre, nationality)
                .map(mapper::toDto);
        return ResponseEntity.ok().body(books);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListBookDTO> show(@PathVariable String id) {
        return ResponseEntity.ok().body(mapper.toDto(bookService.show(UUID.fromString(id))));
    }

    @PostMapping
    public ResponseEntity<ListBookDTO> create(@Valid @RequestBody CreateBookDTO data, UriComponentsBuilder uri) {
        Book book = bookService.create(mapper.toEntity(data));

        URI uriBuilder = uri.path("/books/{id}").buildAndExpand(book.getId()).toUri();

        return ResponseEntity.created(uriBuilder).body(mapper.toDto(book));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListBookDTO> update(@PathVariable String id, @Valid @RequestBody CreateBookDTO data) {
        Book book = bookService.update(UUID.fromString(id), mapper.toEntity(data));
        return ResponseEntity.ok().body(mapper.toDto(book));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ListBookDTO> delete(@PathVariable String id) {
        bookService.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }

}
