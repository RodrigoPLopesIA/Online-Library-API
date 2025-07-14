package br.com.rodrigo.onlinelibraryapi.controllers;

import java.sql.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rodrigo.onlinelibraryapi.dtos.author.CreateAuthorDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.author.ListAuthorDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.books.ListBookDTO;
import br.com.rodrigo.onlinelibraryapi.entities.Author;
import br.com.rodrigo.onlinelibraryapi.exceptions.ErrorMessage;
import br.com.rodrigo.onlinelibraryapi.exceptions.UniqueViolationException;
import br.com.rodrigo.onlinelibraryapi.mapper.AuthorMapper;
import br.com.rodrigo.onlinelibraryapi.services.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;

@Tag(name = "Author", description = "managing authors-related operations in the Online Library API. Provides endpoints to create, retrieve, update, and delete books.")
@RestController
@RequestMapping("api/v1/authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Autowired
    private AuthorMapper authorMapper;

    @Operation(summary = "Get all Authors", responses = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of authors", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = ListAuthorDTO.class))))
    })
    @GetMapping
    public ResponseEntity<Page<ListAuthorDTO>> index(
            Pageable pageable,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "dateBirth", required = false) Date dateBirth,
            @RequestParam(name = "nationality", required = false) String nationality) {
        Page<ListAuthorDTO> authors = authorService.index(pageable, name, nationality, dateBirth)
                .map(authorMapper::toListAuthorDTO);
        return ResponseEntity.ok(authors);
    }

    @Operation(summary = "Retrieves a author by id", responses = {
            @ApiResponse(responseCode = "200", description = "Retrieves a author successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListAuthorDTO.class))),
            @ApiResponse(responseCode = "404", description = "author not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
    })
    @GetMapping("/{id}")
    public ResponseEntity<ListAuthorDTO> show(@PathVariable String id) {
        return ResponseEntity.ok(authorMapper.toListAuthorDTO(authorService.show(UUID.fromString(id))));
    }

    @Operation(summary = "Create a author", responses = {
            @ApiResponse(responseCode = "201", description = "Create author successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListBookDTO.class))),
            @ApiResponse(responseCode = "409", description = "author already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UniqueViolationException.class))),
            @ApiResponse(responseCode = "422", description = "Resource not processed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidException.class))),
    })
    @PostMapping
    public ResponseEntity<ListAuthorDTO> create(@RequestBody CreateAuthorDTO data, UriComponentsBuilder uriBuilder) {
        Author author = authorService.create(authorMapper.toAuthor(data));

        var uri = uriBuilder.path("/api/v1/authors/{id}").buildAndExpand(author.getId()).toUri();

        return ResponseEntity.created(uri).body(authorMapper.toListAuthorDTO(author));

    }

    @Operation(summary = "Update a author", responses = {
            @ApiResponse(responseCode = "200", description = "Update author successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListBookDTO.class))),
            @ApiResponse(responseCode = "409", description = "author already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UniqueViolationException.class))),
            @ApiResponse(responseCode = "422", description = "Resource not processed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MethodArgumentNotValidException.class))),
            @ApiResponse(responseCode = "404", description = "Author not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundException.class))), })
    @PutMapping("/{id}")
    public ResponseEntity<ListAuthorDTO> update(@PathVariable String id, @RequestBody CreateAuthorDTO data) {
        Author author = authorService.update(UUID.fromString(id), authorMapper.toAuthor(data));
        return ResponseEntity.ok(authorMapper.toListAuthorDTO(author));
    }
    @Operation(summary = "delete author by id", responses = {
            @ApiResponse(responseCode = "204", description = "delete a author successfully.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ListBookDTO.class))),
            @ApiResponse(responseCode = "404", description = "author not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EntityNotFoundException.class))),
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<ListAuthorDTO> delete(@PathVariable String id) {
        authorService.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
