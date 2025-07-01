package br.com.rodrigo.onlinelibraryapi.controllers;

import java.sql.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
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
import br.com.rodrigo.onlinelibraryapi.entities.Author;
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
            @RequestParam(name = "dateBirth", required = false) Date dateBirth,
            @RequestParam(name = "nationality", required = false) String nationality) {
        Page<ListAuthorDTO> authors = authorService.index(pageable, name, nationality, dateBirth);
        return ResponseEntity.ok(authors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListAuthorDTO> show(@PathVariable String id) {
        return ResponseEntity.ok(new ListAuthorDTO(authorService.show(UUID.fromString(id))));
    }

    @PostMapping
    public ResponseEntity<ListAuthorDTO> create(@RequestBody CreateAuthorDTO data, UriComponentsBuilder uriBuilder) {
        Author author = authorService.create(data);

        var uri = uriBuilder.path("/api/v1/authors/{id}").buildAndExpand(author.getId()).toUri();

        return ResponseEntity.created(uri).body(new ListAuthorDTO(author));

    }

    @PutMapping("/{id}")
    public ResponseEntity<ListAuthorDTO> update(@PathVariable String id, @RequestBody CreateAuthorDTO data) {
        Author author = authorService.update(UUID.fromString(id), data);
        return ResponseEntity.ok(new ListAuthorDTO(author));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<ListAuthorDTO> delete(@PathVariable String id) {
        authorService.delete(UUID.fromString(id));
        return ResponseEntity.noContent().build();
    }
}
