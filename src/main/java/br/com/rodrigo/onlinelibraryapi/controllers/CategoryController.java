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

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<ListCategoryDTO>> index(Pageable page,
            @RequestParam(name = "name", required = false) String name) {
        Page<ListCategoryDTO> index = categoryService.index(page, name);
        return ResponseEntity.ok().body(index);
    }

    @PostMapping
    public ResponseEntity<ListCategoryDTO> postMethodName(@Valid @RequestBody CreateCategoryDTO data,
            UriComponentsBuilder uriBuilder) {
        ListCategoryDTO entity = this.categoryService.save(data);

        URI uri = uriBuilder.fromUriString("/api/v1/categories/{id}").buildAndExpand(entity.id()).toUri();

        return ResponseEntity.created(uri).body(entity);
    }

}
