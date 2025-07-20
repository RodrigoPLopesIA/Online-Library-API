package br.com.rodrigo.onlinelibraryapi.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.rodrigo.onlinelibraryapi.dtos.category.CreateCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.dtos.category.ListCategoryDTO;
import br.com.rodrigo.onlinelibraryapi.services.CategoryService;
import jakarta.validation.Valid;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<ListCategoryDTO>> index(Pageable page,
            @RequestParam(name = "name", required = false) String name) {
        Page<ListCategoryDTO> response = categoryService.index(page, name);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<ListCategoryDTO> create(@Valid @RequestBody CreateCategoryDTO data,
            UriComponentsBuilder uriBuilder) {
        ListCategoryDTO response = this.categoryService.save(data);

        URI uri = uriBuilder.fromUriString("/api/v1/categories/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ListCategoryDTO> update(@PathVariable String id, @Valid @RequestBody CreateCategoryDTO data) {
        ListCategoryDTO response = this.categoryService.update(id, data);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListCategoryDTO> show(@PathVariable String id) {
        ListCategoryDTO response = this.categoryService.show(id);

        return ResponseEntity.ok().body(response);
    }

}
